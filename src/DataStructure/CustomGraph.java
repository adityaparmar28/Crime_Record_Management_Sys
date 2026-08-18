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
package DataStructure;

public class CustomGraph
{
    public class LinkNode
    {
        public String criminalName;
        public String caseID;
        public LinkNode next;

        public LinkNode(String name, String caseID)
        {
            this.criminalName = name;
            this.caseID = caseID;
            this.next = null;
        }
    }

    public class CriminalNode
    {
        public String criminalName;
        public LinkNode neighborsHead;
        public CriminalNode next;

        public CriminalNode(String name)
        {
            this.criminalName = name;
            this.neighborsHead = null;
            this.next = null;
        }
    }

    public CriminalNode head = null;

    public void addCriminal(String name)
    {
        if (findCriminal(name) != null)
        {
            return;
        }
        CriminalNode newNode = new CriminalNode(name);
        newNode.next = head;
        head = newNode;
    }

    public CriminalNode findCriminal(String name)
    {
        CriminalNode temp = head;
        while (temp != null)
        {
            if (temp.criminalName.equalsIgnoreCase(name))
            {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    public void connectCriminals(String name1, String name2, String caseID)
    {
        addCriminal(name1);
        addCriminal(name2);

        CriminalNode node1 = findCriminal(name1);
        CriminalNode node2 = findCriminal(name2);

        // Add node2 to node1's neighbors list
        LinkNode n1 = new LinkNode(name2, caseID);
        n1.next = node1.neighborsHead;
        node1.neighborsHead = n1;

        // Add node1 to node2's neighbors list
        LinkNode n2 = new LinkNode(name1, caseID);
        n2.next = node2.neighborsHead;
        node2.neighborsHead = n2;
    }

    public void displayNetwork()
    {
        if (head == null)
        {
            System.out.println("[INFO] No gang/accomplice network loaded.");
            return;
        }

        System.out.println("\n================ CRIMINAL ASSOCIATIONS NETWORK ================");
        CriminalNode temp = head;
        while (temp != null)
        {
            System.out.print("Criminal: " + temp.criminalName + " -> ");
            LinkNode neighbor = temp.neighborsHead;
            if (neighbor == null)
            {
                System.out.println("No known accomplices.");
            }
            else
            {
                while (neighbor != null)
                {
                    System.out.print(neighbor.criminalName + " (via Case: " + neighbor.caseID + ") ");
                    if (neighbor.next != null) System.out.print(", ");
                    neighbor = neighbor.next;
                }
                System.out.println();
            }
            temp = temp.next;
        }
        System.out.println("==============================================================");
    }
}
