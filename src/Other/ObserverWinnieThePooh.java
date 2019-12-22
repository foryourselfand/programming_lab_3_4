package Other;

import Utils.Breath;
import Utils.Displayable;

import java.util.HashMap;

public final class ObserverWinnieThePooh implements Displayable {
	private Observable observable;
	private Breath breath;
	
	public ObserverWinnieThePooh(Observable observable, Breath breath) {
		this.observable = observable;
		this.breath = breath;
		this.breathe();
	}
	
	public ObserverWinnieThePooh(Observable observable) {
		this(observable, Breath.STRONG);
	}
	
	@Override
	public String getDisplayableMessage() {
		return "Вздыхает: " + this.breath;
	}
	
	public void breathe() {
//		this.monitor.display(this);
	}
	
	public class MonitorMessage implements Observer {
		private String lastMessage;
		
		public MonitorMessage() {
			observable.addObserver(this);
		}
		
		@Override
		public String getDisplayableMessage() {
			return "Сообщение: " + this.lastMessage;
		}
		
		@Override
		public void update(String message) {
			this.lastMessage = message;
		}
	}
	
	private abstract class Comparator implements Observer {
		protected boolean[] flags;
		protected int previousPattern;
		private int previousLength;
		
		public Comparator() {
			observable.addObserver(this);
			
			flags = new boolean[3];
			previousLength = - 1;
			previousPattern = 42;
		}
		
		@Override
		public void update(String message) {
			int currentLength = message.length();
			
			if (previousLength != - 1) {
				int currentPattern = Integer.compare(currentLength, previousLength);
				previousPattern = currentPattern;
				flags[currentPattern + 1] = true;
			}
			
			previousLength = currentLength;
		}
		
		@Override
		public String getDisplayableMessage() {
			if (previousPattern == 42)
				return getMessageForFirstElementSituation();
			return getMessage();
		}
		
		public abstract String getMessageForFirstElementSituation();
		
		public abstract String getMessage();
	}
	
	public class Length extends Comparator {
		private HashMap<Integer, String> patternToName;
		
		public Length() {
			super();
			patternToName = new HashMap<>();
			patternToName.put(- 1, "меньше предыдущего");
			patternToName.put(0, "равен предыдущему");
			patternToName.put(1, "больше предыдущего");
		}
		
		@Override
		public String getMessageForFirstElementSituation() {
			return "Добавленный элемент не с чем сравнивать.";
		}
		
		@Override
		public String getMessage() {
			String currentState = patternToName.get(previousPattern);
			return "Добавленный элемент " +
					currentState +
					".";
		}
	}
	
	public class Sequence extends Comparator {
		@Override
		public String getMessageForFirstElementSituation() {
			return "Последовательность из единственного элемента.";
		}
		
		@Override
		public String getMessage() {
			StringBuilder allStateBuilder = new StringBuilder("Последовательность ");
			if (flags[0] && flags[2])
				allStateBuilder.append("чередующаяся");
			else if (flags[0] || flags[2]) {
				if (flags[0])
					allStateBuilder.append("убывающая");
				else
					allStateBuilder.append("возрастающая");
				if (flags[1])
					allStateBuilder.append(". С повторяющимися элементами");
			} else if (flags[1])
				allStateBuilder.append("монотонная");
			allStateBuilder.append(".");
			return allStateBuilder.toString();
		}
	}
	
	public class MonitorBlank implements Observer {
		public MonitorBlank() {
			observable.addObserver(this);
		}
		
		@Override
		public String getDisplayableMessage() {
			return "";
		}
		
		@Override
		public void update(String message) {
		}
	}
}
