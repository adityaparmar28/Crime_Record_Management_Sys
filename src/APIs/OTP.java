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

public class OTP
{
    private static final int OTP_VALIDITY_SECONDS = 30;

    // A simple thread to read console input without blocking the main execution indefinitely
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
                // Ignore scanner reading exceptions
            }
        }
    }

    public static boolean sendAndVerifyOTP()
    {
        Random random = new Random();
        int generatedOtp = 100000 + random.nextInt(900000);

        System.out.println("+-------------------------------------------------+");
        System.out.println("  | [OTP SERVICE] Generated OTP: "+generatedOtp+" |");
        System.out.println("  | This OTP is valid for "+OTP_VALIDITY_SECONDS+ " sec....");
        System.out.println("+-------------------------------------------------+");

        System.out.print("Enter OTP: ");

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

            if (secondsPassed >= OTP_VALIDITY_SECONDS)
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
            System.out.println("[TIMEOUT] Time limit exceeded (" + OTP_VALIDITY_SECONDS + "s)! OTP is expired....");
            return false;
        }

        String enteredOtp = readerThread.input.trim();
        if (enteredOtp.isEmpty())
        {
            System.out.println("[INVALID] OTP cannot be empty....");
            return false;
        }

        try
        {
            int inputOtp = Integer.parseInt(enteredOtp);
            if (inputOtp == generatedOtp)
            {
                System.out.println("[SUCCESS] OTP Verified Successfully..!!");
                return true;
            }
            else
            {
                System.out.println("[MISMATCH] Invalid OTP entered....");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("[INVALID] OTP must be a numeric 6-digit....");
            return false;
        }
    }
}
