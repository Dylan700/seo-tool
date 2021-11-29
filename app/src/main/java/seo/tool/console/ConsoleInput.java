package seo.tool.console;

import java.util.Scanner;

public class ConsoleInput implements InputSystem {

    Scanner scanner;

    public ConsoleInput(){
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
