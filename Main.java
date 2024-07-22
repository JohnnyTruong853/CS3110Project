import java.util.*;

public class Main {
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
    public Main(boolean[] finalStates, Set<Character> alphabet, List<Transition> transitions) {
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
            } else {
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

    /*
     * Main method for testing the finite automaton.
     * args command line arguments.
     */
    public static void main(String[] args) {
        boolean[] finalStates = { false, true }; // State 1 is final
        Set<Character> alphabet = new HashSet<>(Arrays.asList('0', '1'));
        List<Transition> transitions = Arrays.asList(
            new Transition(0, '0', 1),
            new Transition(0, '1', 0),
            new Transition(1, '0', 1),
            new Transition(1, '1', 0)
        );

        Main fa = new Main(finalStates, alphabet, transitions);

        String[] testStrings = { "", "100", "011", "0", "1", "0101011", "11010", "0001", "1110" };
        for (String test : testStrings) {
            System.out.print(test + " ");
            fa.run(test);
        }
    }
}

/*
 * Class representing a transition in the finite automaton.
 */
class Transition {
    int fromState;
    char symbol;
    int toState;

    /*
     * Constructor for the transition class.
     * fromState is the state from which the transition originates.
     * symbol the symbol that triggers the transition.
     * toState the state to which the transition goes.
     */
    public Transition(int fromState, char symbol, int toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
}
