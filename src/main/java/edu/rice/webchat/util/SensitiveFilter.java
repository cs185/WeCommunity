package edu.rice.webchat.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SensitiveFilter {
    private class Trie {
        private final Node root;

        public Trie() {
            root = new Node('\0');
        }

        public void add(String word) {
            Node p = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (p.next.containsKey(c)) {
                    p = p.next.get(c);
                }
                else {
                    Node newNode = new Node(c);
                    p.next.put(c, newNode);
                    p = newNode;
                }
            }
            p.isTarget = true;
        }

        public int check(String word) {
            Node p = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (p.next.containsKey(c)) {
                    p = p.next.get(c);
                }
                else
                    return 0;
            }
            // 0: the word is safe
            // 1: the word is not a target word but is not safe
            // 2: the word is a target word
            return p.isTarget ? 2 : 1;
        }
    }

    private class Node {
        char data;
        boolean isTarget;
        Map<Character, Node> next;

        public Node(char data) {
            this.data = data;
            this.next = new HashMap<>();
            this.isTarget = false;
        }
    }

    private Trie trie;

    public SensitiveFilter() {
        trie = new Trie();
    }

    public void addWord(String word) {
        trie.add(word);
    }

    public int checkWord(String word) {
        return trie.check(word);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text))
            return null;

        StringBuilder sb = new StringBuilder();

        int p1 = 0, p2;

        while (p1 < text.length()) {
            p2 = p1 + 1;
            while (p2 <= text.length()) {
                String word = text.substring(p1, p2);
                if (checkWord(word) == 2) {
//                    System.out.println("sensitive: " + text.substring(p1, p2));
                    sb.append("***");
                    p1 = p2;
                    break;
                }
                else if (p2 == text.length() || checkWord(word) == 0) {
//                    System.out.println("good: " + text.substring(p1, p2));
                    sb.append(text, p1, p1 + 1);
                    p1++;
                    break;
                }
                p2++;
            }

        }
        return sb.toString();
    }
}
