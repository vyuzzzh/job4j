package ru.job4j.tracker;

import java.util.*;
import java.util.function.Consumer;

/**
 * Внешний класс, добавляющий итем
 */
class  AddItem extends BaseAction {

    public AddItem(int key, String name, Consumer<String> output) {
        super(key, name, output);
    }

    @Override
    public void execute(Input input, Tracker tracker) {
        this.output.accept("------------ Добавление новой заявки --------------");
        String name = input.ask("Введите имя заявки :");
        String desc = input.ask("Введите описание заявки :");
        Item item = new Item(name, desc);
        tracker.add(item);
        this.output.accept("------------ Новая заявка с Id : " + item.getId() + " -----------");
    }
}

public class MenuTracker {
    /**
     * @param хранит ссылку на объект .
     */
    private Input input;
    /**
     * @param хранит ссылку на объект .
     */
    private Tracker tracker;
    private final Consumer<String> output;
    /**
     * @param хранит ссылку на массив типа UserAction.
     */
    private List<UserAction> actions = new ArrayList<>();

    /**
     * Конструктор.
     *
     * @param input   объект типа Input
     * @param tracker объект типа Tracker
     */
    public MenuTracker(Input input, Tracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }

    /**
     * Метод для получения длины массива действий в меню
     *
     * @return длину массива
     */
    public int getActionsLength() {
        return this.actions.size();
    }
    /**
     * Метод заполняет массив.
     */
    public void fillActions(StartUI ui) {
        this.actions.add(new AddItem(0, "Добавить заявку", output));
        this.actions.add(new ShowItems(1, "Вывод заявок на экран", output));
        this.actions.add(new EditItems(2, "Редактировать заявку", output));
        this.actions.add(new DeleteItem(3, "Удалить заявку", output));
        this.actions.add(new FindItemById(4, "Поиск заявки по id", output));
        this.actions.add(new FindItemsByName(5, "Поиск заявки по имени", output));
        this.actions.add(new ExitProgram(6, "Выход", ui, output));
    }

    /**
     * Метод в зависимости от указанного ключа, выполняет соотвествующие действие.
     *
     * @param key ключ операции
     */
    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    /**
     * Метод выводит на экран меню.
     */
    public void show() {
        for (UserAction action : this.actions) {
            if (action != null) {
                output.accept(action.info());
            }
        }
    }
    private class ShowItems extends BaseAction {

        public ShowItems(int key, String name, Consumer<String> output) {
            super(key, name, output);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            List<Item> items = tracker.findAll();
            if (items.size() > 0) {

                output.accept("------------ Список всех заявок --------------");
                for (Item item : items) {
                    output.accept(String.format("ID - %s | Имя - %s | Описание - %s %s",
                            item.getId(), item.getName(), item.getDesc(), System.lineSeparator()));
                }
            } else {
                output.accept("------------ Заявки отсутствуют ------------" + System.lineSeparator());
            }
        }
    }

    /**
     * Внутренний класс, изменяющий итем.
     */
    private class EditItems extends BaseAction {

        public EditItems(int key, String name, Consumer<String> output) {
            super(key, name, output);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            output.accept("------------ Редактирование заявки --------------");
            String id = input.ask("Введите id редактируемой заявки");
            String nameReplace = input.ask("Введите имя новой заявки :");
            String descReplace = input.ask("Введите описание новой заявки :");
            Item item = new Item(nameReplace, descReplace);
            item.setId(id);

            if (tracker.replace(item)) {
                output.accept("------------ Новая заявка с Id : " + item.getId() + " -----------" + System.lineSeparator());
            } else  {
                output.accept("Заявки с таким id не существует." + System.lineSeparator());
            }
        }
    }

    private class DeleteItem extends BaseAction {

        public DeleteItem(int key, String name, Consumer<String> output) {
            super(key, name, output);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            output.accept("------------ Удаление заявки --------------");
            String id = input.ask("Введите id заявки, которую вы хотите удалить");
            if (tracker.delete(tracker.findById(id))) {
                output.accept("------------ Новая заявка с Id : " + id + " успешно удалена -----------" + System.lineSeparator());
            } else {
                output.accept("Заявки с таким id не существует." + System.lineSeparator());
            }
        }
    }

    private class FindItemById extends BaseAction {

        public FindItemById(int key, String name, Consumer<String> output) {
            super(key, name, output);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            output.accept("------------ Поиск заявки по id --------------");
            String id = input.ask("Введите id заявки, которую вы хотите найти");
            Item item = tracker.findById(id);
            if (item != null) {
                output.accept(String.format("ID - %s | Имя - %s | Описание - %s %s",
                        item.getId(), item.getName(), item.getDesc(), System.lineSeparator()));
            } else {
                output.accept("Заявка не найдена" + System.lineSeparator());
            }
        }
    }

    private class FindItemsByName extends BaseAction {

        public FindItemsByName(int key, String name, Consumer<String> output) {
            super(key, name, output);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            output.accept("------------ Поиск заявки по имени --------------");
            String name = input.ask("Введите имя заявки, которую вы хотите найти");
            List<Item> items = tracker.findByName(name);
            if (!items.isEmpty()) {
                for (Item item : items) {
                    if (item != null) {
                        output.accept(String.format("ID - %s | Имя - %s | Описание - %s %s",
                                item.getId(), item.getName(), item.getDesc(), System.lineSeparator()));
                    } else {
                        output.accept("Заявка не найдена" + System.lineSeparator());
                    }
                }
            } else {
                output.accept("Заявка не найдена" + System.lineSeparator());
            }

        }
    }

    private static class ExitProgram extends BaseAction {

        private final StartUI ui;

        public ExitProgram(int key, String name, StartUI ui, Consumer<String> output) {
            super(key, name, output);
            this.ui = ui;
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            this.ui.stop();
        }
    }
}
