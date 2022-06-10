package server.commands;

import server.connection_control.User;

public class PrintFieldAscendingGovernment extends Command {
    PrintFieldAscendingGovernment() {
        super("print_field_ascending_government", "",
                "выводит значения поля government всех элементов в порядке возрастания", null, null, false);
    }

    /**
     * print field government from all cities
     *
     * @param programController that uses for program
     * @param args              for command from console input (args[0] is program name)
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args) {
        StringBuilder data = new StringBuilder();
        programController.getDataController().readLock();
        programController.getDataController().getMap().values().stream().sorted((o1, o2) -> {
            if (o1.getGovernment() == null && o2.getGovernment() == null)
                return 0;
            if (o1.getGovernment() == null)
                return -1;
            if (o2.getGovernment() == null)
                return 1;
            return Integer.compare(o1.getGovernment().ordinal() - o2.getGovernment().ordinal(), 0);
        }).forEach(city -> data.append("id ").append(city.getId()).append(": ")
                .append(city.getGovernmentString()).append("\n"));
        programController.getDataController().readUnlock();
        return data.toString();
    }
}
