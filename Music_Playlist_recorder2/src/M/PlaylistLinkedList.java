package M;

import java.io.*;

public class PlaylistLinkedList {
    // ... Keep your existing Node, head, tail, current variables and methods exactly the same ...
    
    public static class Node {
        Track track;
        Node next;
        Node prev;
        public Node(Track track) { this.track = track; }
    }

    private Node head = null;
    private Node tail = null;
    private Node current = null;

    public void addTrack(Track track) {
        Node newNode = new Node(track);
        if (head == null) { head = tail = current = newNode; } 
        else { tail.next = newNode; newNode.prev = tail; tail = newNode; }
    }

    public Node getAtIndex(int index) {
        if (index < 0 || head == null) return null;
        Node temp = head; int count = 0;
        while (temp != null) {
            if (count == index) { current = temp; return temp; }
            temp = temp.next; count++;
        }
        return null;
    }

    public Node getCurrent() { return current; }
    public Node nextSong() { if (current != null && current.next != null) current = current.next; return current; }
    public Node prevSong() { if (current != null && current.prev != null) current = current.prev; return current; }

    // Clear the linked list memory entirely to start fresh
    public void clear() {
        head = null;
        tail = null;
        current = null;
    }

    // TRAVERSAL METHOD: Walks the linked list and saves node paths into a flat data text file
    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Node temp = head;
            while (temp != null) {
                // Save title and file path separated by a pipe symbol
                writer.write(temp.track.getTitle() + "|" + temp.track.getFilePath());
                writer.newLine();
                temp = temp.next;
            }
        }
    }

    // IMPORT METHOD: Reads a flat text data file line-by-line and builds nodes sequentially
    public void loadFromFile(File file) throws IOException {
        clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    addTrack(new Track(parts[0], parts[1]));
                }
            }
        }
    }
}
