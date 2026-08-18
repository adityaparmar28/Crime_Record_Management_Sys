/*
 * Copyright 2026 MR. ADITYA PARMAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package APIs;

import java.util.Scanner;
import java.util.Random;

public class Captcha
{
    private static final int CAPTCHA_VALIDITY_SECONDS = 30;

    private static class InputReader extends Thread
    {
        public String input = "";
        public boolean hasInput = false;

        @Override
        public void run()
        {
            try
            {
                Scanner sc = new Scanner(System.in);

                if (sc.hasNextLine())
                {
                    input = sc.nextLine();
                    hasInput = true;
                }
            }
            catch (Exception e)
            {
                // Ignore scanner reading exceptions....
            }
        }
    }

    public static boolean verifyCaptcha()
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder captcha = new StringBuilder();
        Random rnd = new Random();

        while (captcha.length() < 5)
        {
            int index = (int) (rnd.nextFloat() * chars.length());
            captcha.append(chars.charAt(index));
        }
        
        String generatedCaptcha = captcha.toString();

        System.out.println("+-------------------------------------------------+");
        System.out.println("--------[TWO STEP AUTHENTICATION REQUIRED]---------");
        System.out.println("        | CAPTCHA: " + generatedCaptcha + " |");
        System.out.println("-| This CAPTCHA is valid for " + CAPTCHA_VALIDITY_SECONDS + " seconds....");
        System.out.println("+-------------------------------------------------+");

        System.out.print("Enter CAPTCHA: ");

        // Start background input reader thread
        InputReader readerThread = new InputReader();
        readerThread.setDaemon(true);
        readerThread.start();

        long startTime = System.currentTimeMillis();
        boolean isTimeout = false;

        // Loop until user enters input or 30 seconds pass
        while (readerThread.hasInput == false)
        {
            long currentTime = System.currentTimeMillis();
            long secondsPassed = (currentTime - startTime) / 1000;

            if (secondsPassed >= CAPTCHA_VALIDITY_SECONDS)
            {
                isTimeout = true;
                break;
            }

            try
            {
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                // Ignore sleep interruption
            }
        }

        if (isTimeout)
        {
            System.out.println("[TIMEOUT] Time limit exceeded (" + CAPTCHA_VALIDITY_SECONDS + "s)! CAPTCHA has expired....");
            return false;
        }

        String userInput = readerThread.input.trim();
        if (userInput.isEmpty())
        {
            System.out.println("[INVALID] CAPTCHA can't be empty....");
            return false;
        }

        if (userInput.equals(generatedCaptcha))
        {
            System.out.println("[SUCCESS] CAPTCHA Verified..!!");
            return true;
        }
        else
        {
            System.out.println("[ERROR] CAPTCHA verification failed....");
            return false;
        }
    }
}
