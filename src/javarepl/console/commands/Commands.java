package javarepl.console.commands;


import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.yadic.Container;
import javarepl.console.ConsoleConfig;

public final class Commands {
    private final Container context;

    private Sequence<Class<? extends Command>> commands = Sequences.empty();

    public Commands(Container context, ConsoleConfig config) {
        this.context = context;
        this.commands = config.commands;
    }

    public Sequence<Command> allCommands() {
        return userCommands()
                .join(commandInstances(Sequences.<Class<? extends Command>>sequence(
                        NotAValidCommand.class,
                        ShowResult.class,
                        EvaluateExpression.class)));
    }

    Sequence<Command> userCommands() {
        return commandInstances(commands.cons(ShowHelp.class));
    }

    private Sequence<Command> commandInstances(Sequence<Class<? extends Command>> commands) {
        return commands.map(createCommandInstance(context));
    }

    public static final Function1<Class<? extends Command>, Command> createCommandInstance(final Container context) {
        return new Function1<Class<? extends Command>, Command>() {
            public Command call(Class<? extends Command> aClass) throws Exception {
                return context.create(aClass);
            }
        };
    }

    public static Class<? extends Command>[] defaultCommands() {
        return Sequences.<Class<? extends Command>>sequence()
                .add(QuitApplication.class)
                .add(ShowHistory.class)
                .add(SearchHistory.class)
                .add(EvaluateFromHistory.class)
                .add(ResetAllEvaluations.class)
                .add(ReplayAllEvaluations.class)
                .add(AddToClasspath.class)
                .add(LoadSourceFile.class)
                .add(ListValues.class)
                .add(ShowLastSource.class)
                .add(ShowTypeOfExpression.class)
                .toArray(Command.class.getClass());
    }

}
