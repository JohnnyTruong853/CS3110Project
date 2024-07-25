import java.util.*;
import java.io.*;

public class FiniteAutomaton {
    private boolean[] finalStates;
    private Set<Character> alphabet;
    private Map<Integer, Map<Character, Integer>> transitionTable;
    private int currentState;

    /*
     * Constructor for the Main class which represents the universal finite automaton.
     * finalStates is array indicating which states are final. (Note: this is only for accepting states)
     * alphabet is for a set of characters representing the alphabet.
     * transitions is for list of transitions defining the state changes.
     */
    public FiniteAutomaton(boolean[] finalStates, Set<Character> alphabet, List<Transition> transitions) {
        this.finalStates = finalStates;
        this.alphabet = alphabet;
        this.transitionTable = new HashMap<>();

        for (Transition transition : transitions) {
            transitionTable.computeIfAbsent(transition.fromState, k -> new HashMap<>())
                           .put(transition.symbol, transition.toState);
        }

        this.currentState = 0; // Initial state
    }

    /*
     * Runs the finite automaton with a given input string and determines if it is accepted or rejected.
     * input string to be processed by the finite automaton.
     */
    public void run(String input) {
        reset();

        for (char symbol : input.toCharArray()) {
            if (alphabet.contains(symbol)) {
                currentState = nextState(currentState, symbol);
                if (currentState == -1) {
                    reject();
                    return;
                }
            } 
            else {
                reject();
                return;
            }
        }

        if (finalStates[currentState]) {
            accept();
        } else {
            reject();
        }
    }

    /*
     * Determines the next state given the current state and input symbol.
     * state the current state.
     * symbol the input symbol.
     * return the next state, or -1 if no valid transition exists.
     */
    private int nextState(int state, char symbol) {
        return transitionTable.getOrDefault(state, Collections.emptyMap()).getOrDefault(symbol, -1);
    }

    /*
     * Resets the finite automaton to the initial state.
     */
    private void reset() {
        currentState = 0; // Reset to initial state
    }

    /*
     * Handles the acceptance of the input string.
     */
    private void accept() {
        System.out.println("Accept");
    }

    /*
     * Handles the rejection of the input string.
     */
    private void reject() {
        System.out.println("Reject");
    }

    // Main method for testing the finite automaton
    public static void main(String[] args) {
        // Read input from a file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("automaton.txt"));

            String line;
            while ((line = reader.readLine()) != null) {

                // Read number of states
                int numOfStates = Integer.parseInt(line.trim());
                //System.out.println(numOfStates);

                // Initialize set of states
                int[] states = new int[numOfStates];
                for (int i = 0; i < numOfStates; i++) {
                    states[i] = i;
                }

                // Read list of final states
                String[] finalStateArray = reader.readLine().trim().split(" ");
                boolean[] finalStates = new boolean[numOfStates];
                for (String state : finalStateArray) {
                    finalStates[Integer.parseInt(state)] = true;
                }

                // Read alphabet
                String[] alphabetArray = reader.readLine().trim().split(" ");
                Set<Character> alphabet = new HashSet<>();
                for (String symbol : alphabetArray) {
                    alphabet.add(symbol.charAt(0));
                }

                // Read transitions
                List<Transition> transitions = new ArrayList<>();
                String tripleString;
                while (!(tripleString = reader.readLine()).equals("*")) {
                    String transitionSubString = tripleString.substring(1, tripleString.length() - 1 ); // Remove the () from the sequence of transitions ie. (p a q)
                    String[] triple = transitionSubString.split(" ");
                    int fromState = Integer.parseInt(triple[0]);
                    char symbol = triple[1].charAt(0);
                    int toState = Integer.parseInt(triple[2]);

                    transitions.add(new Transition(fromState, symbol, toState));
                }

                // Create the finite automaton
                FiniteAutomaton fa = new FiniteAutomaton(finalStates, alphabet, transitions);

                // Read and test input strings
                System.out.println("Inputted Finite State Automaton Info:");
                System.out.print("    1) set of states: ");
                System.out.print(Arrays.toString(states));
                System.out.println(", initial state is state 0 (default).");
                System.out.print("    2) set of final state(s): ");
                System.out.println(Arrays.toString(finalStateArray));
                System.out.println("    3) alphabet set: " + alphabet);
                System.out.println("    4) transitions:");
                for (Transition transition : transitions) {
                    System.out.printf("         %d %c %d\n", transition.fromState, transition.symbol, transition.toState);
                }

                System.out.println("Results of test strings:");
                for (String testString = reader.readLine(); !(".".equals(testString)); testString = reader.readLine()) {
                    System.out.printf("     %-11s", testString);
                    fa.run(testString);
                }

                System.out.println();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Class representing a transition
class Transition {
    int fromState;
    char symbol;
    int toState;

    public Transition(int fromState, char symbol, int toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
}
