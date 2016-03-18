import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KrefAnalyzer {
	
	private static HashMap<String, Integer> krefs = new HashMap<>();
	
	public static void main(String[] args) {
		Pattern createdRegex = Pattern.compile("Created object \\'(.+)\\'");
		Pattern destroyedRegex = Pattern.compile("Destroyed object \\'(.+)\\'");
		
		Scanner scanner = new Scanner(System.in);
		try {
			while (scanner.hasNext()) {
				Matcher matcher;
				String currentLine = scanner.nextLine();
				
				matcher = createdRegex.matcher(currentLine);
				if (matcher.find()) {
					add(matcher.group(1));
					continue;
				}
				
				matcher = destroyedRegex.matcher(currentLine);
				if (matcher.find()) {
					remove(matcher.group(1));
					continue;
				}
			}
			
		} finally {
			scanner.close();
		}
		
		for (String object : krefs.keySet()) {
			System.err.print("Memory leak: ");
			System.err.print(krefs.get(object));
			System.err.print(" instances of object '");
			System.err.print(object);
			System.err.println("' remain.");
		}
	}

	private static void add(String object) {
		Integer count = krefs.get(object);
		if (count != null) {
			krefs.put(object, count + 1);
		} else {
			krefs.put(object, 1);
		}
	}
	
	private static void remove(String object) {
		Integer count = krefs.get(object);
		if (count != null) {
			if (count == 1) {
				krefs.remove(object);
			} else {
				krefs.put(object, count - 1);
			}
		} else {
			System.err.println("Error: Object '" + object + "' was destroyed without being initialized.");
		}
	}
	
}
