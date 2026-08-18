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

public class TimeStamp {
    private static volatile long lastActivityTime = System.currentTimeMillis();

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static long getLastActivityTime() {
        return lastActivityTime;
    }

    public static long getDurationAndReset() {
        long now = System.currentTimeMillis();
        long duration = now - lastActivityTime;
        lastActivityTime = now;
        return duration;
    }

    public static void resetTimer() {
        lastActivityTime = System.currentTimeMillis();
    }
}
