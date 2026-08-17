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

public class CustomBinarySearchTree
{
    public class node
    {
        public String data;
        public node left;
        public node right;

        public node(String data)
        {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public node root = null;

    // Wrapper for compatibility with CRMngrQueries.java

    public void Insert(String data)
    {
        if (data == null)
        {
            return;
        }

        node n = new node(data);

        if (root == null)
        {
            root = n;
        }
        else
        {
            node temp = root;

            while (true)
            {
                if (temp.left == null && temp.data.compareToIgnoreCase(data) > 0)
                {
                    temp.left = n;
                    return;
                }
                else if (temp.right == null && temp.data.compareToIgnoreCase(data) < 0)
                {
                    temp.right = n;
                    return;
                }
                else if (temp.data.compareToIgnoreCase(data) > 0)
                {
                    temp = temp.left;
                }
                else
                {
                    temp = temp.right;
                }
            }
        }
    }

    public void inorder(node temp)
    {
        if (temp == null)
        {
            return;
        }
        else
        {
            inorder(temp.left);
            System.out.print(temp.data + " ");
            inorder(temp.right);
        }
    }

    public String min(node temp)
    {
        while (temp.left != null)
        {
            temp = temp.left;
        }

        return temp.data;
    }

    public String max(node temp)
    {
        while (temp.right != null)
        {
            temp = temp.right;
        }
        return temp.data;
    }

    public void search(String value, node temp)
    {
        if (temp == null)
        {
            System.out.println("[INFO] Database Empty....");
            return;
        }
        else
        {
            while (temp != null)
            {
                if (temp.data.equalsIgnoreCase(value))
                {
                    System.out.println("[INFO] "+value + " is found in Databse....");
                    return;
                }
                else if (temp.data.compareToIgnoreCase(value) > 0)
                {
                    temp = temp.left;
                }
                else if (temp.data.compareToIgnoreCase(value) < 0)
                {
                    temp = temp.right;
                }
            }

            System.err.println("[ERROR] "+value + " is not found in Database....");
        }
    }

    public void delete(String value)
    {
        root = deleteRecursive(root, value);
    }

    public node deleteRecursive(node temp, String value)
    {
        if (temp == null)
        {
            return null;
        }
        else if (temp.data.compareToIgnoreCase(value) > 0)
        {
            temp.left = deleteRecursive(temp.left, value);
        }
        else if (temp.data.compareToIgnoreCase(value) < 0)
        {
            temp.right = deleteRecursive(temp.right, value);
        }
        else
        {
            if (temp.left == null)
            {
                return temp.right;
            }
            else if (temp.right == null)
            {
                return temp.left;
            }

            temp.data = min(temp.right);
            temp.right = deleteRecursive(temp.right, temp.data);
        }
        return temp;
    }

    public String findSuccessor(node temp, String value)
    {
        node succ = null;

        while (temp != null)
        {
            if (temp.data.compareToIgnoreCase(value) > 0)
            {
                succ = temp;
                temp = temp.left;
            }
            else
            {
                temp = temp.right;
            }
        }
        return succ != null ? succ.data : null;
    }

    // Prefix search implementation utilizing the BST properties
    public void searchPrefix(String prefix)
    {
        if (prefix == null || prefix.trim().isEmpty())
        {
            System.out.println("[WARNING] Prefix can't be empty....");
            return;
        }
        System.out.print("Matches starting with '" + prefix + "': ");
        boolean[] found = new boolean[1];

        inorderSearch(root, prefix.toUpperCase(), found);

        if (!found[0])
        {
            System.out.println("[INFO] No matches found for prefix: " + prefix);
        }
    }

    private void inorderSearch(node temp, String prefix, boolean[] found)
    {
        if (temp == null)
        {
            return;
        }

        String currentUpper = temp.data.toUpperCase();

        if (currentUpper.startsWith(prefix))
        {
            inorderSearch(temp.left, prefix, found);
            System.out.println(" -> " + temp.data);
            found[0] = true;
            inorderSearch(temp.right, prefix, found);
        }
        else if (currentUpper.compareTo(prefix) < 0)
        {
            inorderSearch(temp.right, prefix, found);
        }
        else
        {
            inorderSearch(temp.left, prefix, found);
        }
    }
}
