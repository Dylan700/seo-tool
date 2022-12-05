package seo.tool.console.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Basically a map of commands and their string representation
public class CommandRegistry {
    private Map<String, Command> commands = new HashMap<String, Command>();

    public void add(String name, Command command){
        commands.put(name, command);
    }

    public Command get(String name){
        return commands.get(name);
    }

    public Set<Map.Entry<String, Command>> getAll(){
	return commands.entrySet();
    }
}
