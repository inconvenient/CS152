import java.util.*;

class Command {

	private String opcode;
	private List<String> operands;

	public Command(String opcode, List<String> operands) {
		this.opcode = opcode;
		this.operands = operands;
	}

	private String add(List<String> args, Map<String, Integer> env) throws Exception {
		Integer result = 0;
		Integer x = 0;
		for (String arg : args) {
			try {
				x = Integer.parseInt(arg);
			} catch (Exception e) {
				x = env.get(arg);
				if (x == null)
					throw new Exception("undefined variable: " + arg);
			}
			result += x;
		}
		return "" + result;
	}

	private String mul(List<String> args, Map<String, Integer> env) throws Exception {
		Integer result = 1;
		Integer x = 0;
		for (String arg : args) {
			try {
				x = Integer.parseInt(arg);
			} catch (Exception e) {
				x = env.get(arg);
				if (x == null)
					throw new Exception("undefined variable: " + arg);
			}
			result = result * x;
		}
		return "" + result;
	}

	private String load(List<String> args, Map<String, Integer> env) throws Exception {
		String var = args.get(0);
		try {
			int value = Integer.parseInt(args.get(1));
			env.put(var, value);
		} catch (Exception e) {
			Integer x = env.get(args.get(1));
			if (x == null) {
				throw new Exception("undefined variable: " + args.get(1));
			} else {
				env.put(var, x);
			}
		}
		return "done";
	}

	public String execute(Map<String, Integer> env) throws Exception {

		String result;
		if (this.opcode.equals("add")) {
			result = add(this.operands, env);
		} else if (this.opcode.equals("mul")) {
			result = mul(this.operands, env);
		} else if (this.opcode.equals("load")) {
			result = load(this.operands, env);
		} else {
			result = "unrecognized opcode: " + this.opcode;
		}
		return result;
	}
}

public class Console {
	private Scanner kbd = new Scanner(System.in);
	private Map<String, Integer> env = new HashMap<String, Integer>();
	
	public static void main(String args[]) {
		Console console = new Console();
		console.repl();
	}

	private List<String> scan(String exp) {
		ArrayList<String> tokenList = new ArrayList<String>();
		String[] pieces = exp.split("\\s+");
		Collections.addAll(tokenList, pieces);
		return tokenList;
	}

	private Command parse(List<String> tokens) {
		String opCode = tokens.get(0);
		List<String> operands = new ArrayList<String>();
		operands.addAll(tokens);
		operands.remove(0);
		return new Command(opCode, operands);
	}

	// read-execute-print loop
	public void repl() {
		while (true) {
			try {
				System.out.print("-> ");
				String input = kbd.nextLine();
				if (input.equals("quit"))
					break;
				Command cmmd = parse(scan(input));
				System.out.println(cmmd.execute(env));
			} catch (Exception e) {
				System.out.println("Error, " + e.getMessage());
			}
		}
		System.out.println("bye");
	}
}