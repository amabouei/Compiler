package lexical_step;

public class Transition {
    private State start ;
    private InputType input;

    public Transition(State start, InputType input) {
        this.start = start;
        this.input = input;
    }

    @Override
    public int hashCode() {
        return start.getId() * 10001 + input.hashCode();
    }
}
