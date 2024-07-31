import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UniversalFA {
    private int numStates;
    private Set<Integer> finalStates;
    private Set<Character> alphabet;
    private Map<Integer, Map<Character, Integer>> transitions;
    private int currentState;

    public UniversalFA(int numStates, Set<Integer> finalStates, Set<Character> alphabet, Map<Integer, Map<Character, Integer>> transitions) {
        this.numStates = numStates;
        this.finalStates = finalStates;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.currentState = 0; // Initial state is always 0
    }

    public String simulate(String inputString) {
        currentState = 0;
        for (char symbol : inputString.toCharArray()) {
            if (alphabet.contains(symbol)) {
                currentState = nextState(currentState, symbol);
                if (currentState == -1) {
                    return "Reject";
                }
            } else {
                return "Reject";
            }
        }
        return finalStates.contains(currentState) ? "Accept" : "Reject";
    }

    private int nextState(int currentState, char symbol) {
        return transitions.getOrDefault(currentState, new HashMap<>()).getOrDefault(symbol, -1);
    }

    public static void main(String[] args) {
        // M1: Recognizes strings over {0,1} that end with 0
        Set<Character> alphabet01 = new HashSet<>();
        alphabet01.add('0');
        alphabet01.add('1');

        Set<Integer> finalStatesM1 = new HashSet<>();
        finalStatesM1.add(1);

        Map<Integer, Map<Character, Integer>> transitionsM1 = new HashMap<>();
        transitionsM1.put(0, new HashMap<>());
        transitionsM1.put(1, new HashMap<>());
        transitionsM1.get(0).put('0', 1);
        transitionsM1.get(0).put('1', 0);
        transitionsM1.get(1).put('0', 1);
        transitionsM1.get(1).put('1', 0);

        UniversalFA m1 = new UniversalFA(2, finalStatesM1, alphabet01, transitionsM1);
        String[] testStringsM1 = {"", "100", "011", "10abc1", "0", "1", "0101011", "11010", "0001", "1110"};
        System.out.println("Testing M1:");
        for (String s : testStringsM1) {
            System.out.println("String: " + s + ", Result: " + m1.simulate(s));
        }
        System.out.println();

        // M2: Recognizes strings over {0,1} that do not have two consecutive 1's
        Set<Integer> finalStatesM2 = new HashSet<>();
        finalStatesM2.add(0);
        finalStatesM2.add(1);

        Map<Integer, Map<Character, Integer>> transitionsM2 = new HashMap<>();
        transitionsM2.put(0, new HashMap<>());
        transitionsM2.put(1, new HashMap<>());
        transitionsM2.put(2, new HashMap<>());
        transitionsM2.get(0).put('0', 0);
        transitionsM2.get(0).put('1', 1);
        transitionsM2.get(1).put('0', 0);
        transitionsM2.get(1).put('1', 2);
        transitionsM2.get(2).put('0', 2);
        transitionsM2.get(2).put('1', 2);

        UniversalFA m2 = new UniversalFA(3, finalStatesM2, alphabet01, transitionsM2);
        String[] testStringsM2 = {"", "1", "000", "101", "111", "01001", "1011011", "1011000", "01010", "1010101110"};
        System.out.println("Testing M2:");
        for (String s : testStringsM2) {
            System.out.println("String: " + s + ", Result: " + m2.simulate(s));
        }
        System.out.println();

        // M3: Recognizes all identifiers that begin with a letter and followed by any number of letters and digits
        Set<Character> alphabetLettersDigits = new HashSet<>();
        for (char ch = 'a'; ch <= 'z'; ch++) alphabetLettersDigits.add(ch);
        for (char ch = 'A'; ch <= 'Z'; ch++) alphabetLettersDigits.add(ch);
        for (char ch = '0'; ch <= '9'; ch++) alphabetLettersDigits.add(ch);

        Set<Integer> finalStatesM3 = new HashSet<>();
        finalStatesM3.add(1);
        finalStatesM3.add(2);

        Map<Integer, Map<Character, Integer>> transitionsM3 = new HashMap<>();
        transitionsM3.put(0, new HashMap<>());
        transitionsM3.put(1, new HashMap<>());
        transitionsM3.put(2, new HashMap<>());
        for (char ch : alphabetLettersDigits) {
            if (Character.isLetter(ch)) {
                transitionsM3.get(0).put(ch, 1);
                transitionsM3.get(1).put(ch, 1);
                transitionsM3.get(2).put(ch, 2);
            } else {
                transitionsM3.get(1).put(ch, 2);
                transitionsM3.get(2).put(ch, 2);
            }
        }

        UniversalFA m3 = new UniversalFA(3, finalStatesM3, alphabetLettersDigits, transitionsM3);
        String[] testStringsM3 = {"", "HelloWorld", "abc", "1st_Ex", "Java", "the_num", "code", "X3Y7", "X=90", "X*Y"};
        System.out.println("Testing M3:");
        for (String s : testStringsM3) {
            System.out.println("String: " + s + ", Result: " + m3.simulate(s));
        }
        System.out.println();

        // M4: Recognizes all decimal unsigned integers without leading zeros except 0
        Set<Character> alphabetDigits = new HashSet<>();
        for (char ch = '0'; ch <= '9'; ch++) alphabetDigits.add(ch);

        Set<Integer> finalStatesM4 = new HashSet<>();
        finalStatesM4.add(1);
        finalStatesM4.add(2);

        Map<Integer, Map<Character, Integer>> transitionsM4 = new HashMap<>();
        transitionsM4.put(0, new HashMap<>());
        transitionsM4.put(1, new HashMap<>());
        transitionsM4.put(2, new HashMap<>());
        transitionsM4.put(3, new HashMap<>());

        transitionsM4.get(0).put('0', 1);
        for (char ch = '1'; ch <= '9'; ch++) transitionsM4.get(0).put(ch, 2);

        for (char ch = '0'; ch <= '9'; ch++) transitionsM4.get(1).put(ch, 3);
        for (char ch = '0'; ch <= '9'; ch++) transitionsM4.get(2).put(ch, 2);
        for (char ch = '0'; ch <= '9'; ch++) transitionsM4.get(3).put(ch, 3);

        UniversalFA m4 = new UniversalFA(4, finalStatesM4, alphabetDigits, transitionsM4);
        String[] testStringsM4 = {"7", "-7", "007", "3.14", "103", "24930000", "0", "01", "100", "0101"};
        System.out.println("Testing M4:");
        for (String s : testStringsM4) {
            System.out.println("String: " + s + ", Result: " + m4.simulate(s));
        }
        System.out.println();
    }
}
