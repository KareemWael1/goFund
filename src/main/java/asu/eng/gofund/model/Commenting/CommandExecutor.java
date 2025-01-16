package asu.eng.gofund.model.Commenting;

import asu.eng.gofund.model.IIterator;

import java.util.*;

public class CommandExecutor implements IIterator {
    private static final Map<Long, Stack<CommentCommand>> userUndoStack = new HashMap<>();
    private static final Map<Long, Stack<CommentCommand>> userRedoStack = new HashMap<>();
    private static final Map<Long, List<String>> userAuditLog = new HashMap<>();

    public static void executeCommand(CommentCommand command, Long userId) {
        userUndoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
        userRedoStack.computeIfAbsent(userId, k -> new Stack<>()).clear(); // Clear redo stack on new action
        command.execute();
        addAuditLog(userId, "Executed: " + command.getDescription());
    }

    public static void undoLastCommand(Long userId) {
        Stack<CommentCommand> undoStack = userUndoStack.get(userId);
        if (undoStack != null && !undoStack.isEmpty()) {
            CommentCommand command = undoStack.pop();
            command.undo();
            userRedoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
            addAuditLog(userId, "Undone: " + command.getDescription());
        }
    }

    public static void redoLastCommand(Long userId) {
        Stack<CommentCommand> redoStack = userRedoStack.get(userId);
        if (redoStack != null && !redoStack.isEmpty()) {
            CommentCommand command = redoStack.pop();
            command.execute();
            userUndoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
            addAuditLog(userId, "Redone: " + command.getDescription());
        }
    }

    public static List<String>  getAuditLog(Long userId) {
        return userAuditLog.getOrDefault(userId, new ArrayList<>());
    }

    private static void addAuditLog(Long userId, String log) {
        userAuditLog.computeIfAbsent(userId, k -> new ArrayList<>()).add(log);
    }

    @Override
    public Iterator<String> createLogsIterator() {
        return userAuditLog.values().stream()
            .flatMap(Collection::stream)
            .iterator();
    }
}
