import java.util.*;

class Statement {
	private String conclusion;
	private List<String> conditions = new ArrayList<String>();

	public Statement(String... s) {
		String[] blocks = s;
		for (int i = 0; i < s.length; i++) {
			if (i == 0) {
				conclusion = blocks[i];
			} else {
				conditions.add(blocks[i]);
			}
		}
	}

	public boolean matches(String goal) {
		if (goal.equals(conclusion)) {
			return true;
		} else
			return false;
	}

	public List<String> getConditions() {
		return conditions;
	}
}

public class Engine {
	private List<Statement> kbase = new LinkedList<Statement>();
	private Scanner scanner = new Scanner(System.in);

	public void add(Statement s) {
		kbase.add(s);
	}

	public boolean execute(List<String> goals) {
		// 1. if no goals return true
		// 2. traverse kbase looking for statements that match first goal
		// 3. each time one is found recursively execute with goals2 = goals -
		// 1st goal + tail
		// 4. if true is ever returned, stop iteration and return true
		// 5. if nothing works, return false

		if (goals.isEmpty()) {
			return true;
		} else {
			for (String goal : goals) {
				for (Statement st : kbase) {
					if (st.matches(goal)) {
						List<String> newgoals = new LinkedList<String>();
						newgoals.addAll(goals);
						newgoals.addAll(st.getConditions());
						newgoals.remove(0);
						if(execute(newgoals)) {
							return true;
						} else return false;
					}
				}
			}
		}
		return false;
	}

	public void repl() {
		List<String> goals = new LinkedList<String>();
		while (true) {
			System.out.print("?- ");
			String query = scanner.next();
			if (query.equals("quit")) {
				System.out.println("bye");
				break;
			}
			goals.clear();
			goals.add(query);
			boolean result = execute(goals);
			System.out.println(result);
		}
	}

	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.add(new Statement("homerIsMale"));
		engine.add(new Statement("bartIsMale"));
		engine.add(new Statement("homerIsParentOfBart"));
		engine.add(new Statement("homerIsFatherOfBart", "homerIsMale", "homerIsParentOfBart"));
		engine.repl();
	}
}