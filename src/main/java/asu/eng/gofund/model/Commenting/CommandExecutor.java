package asu.eng.gofund.model.Commenting;

import java.util.*;

public class CommandExecutor {
    private final Map<Long, Stack<CommentCommand>> userUndoStack = new HashMap<>();
    private final Map<Long, Stack<CommentCommand>> userRedoStack = new HashMap<>();
    private final Map<Long, List<String>> userAuditLog = new HashMap<>();

    public void executeCommand(CommentCommand command, Long userId) {
        userUndoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
        userRedoStack.computeIfAbsent(userId, k -> new Stack<>()).clear(); // Clear redo stack on new action
        command.execute();
        addAuditLog(userId, "Executed: " + command.getDescription());
    }

    public void undoLastCommand(Long userId) {
        Stack<CommentCommand> undoStack = userUndoStack.get(userId);
        if (undoStack != null && !undoStack.isEmpty()) {
            CommentCommand command = undoStack.pop();
            command.undo();
            userRedoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
            addAuditLog(userId, "Undone: " + command.getDescription());
        }
    }

    public void redoLastCommand(Long userId) {
        Stack<CommentCommand> redoStack = userRedoStack.get(userId);
        if (redoStack != null && !redoStack.isEmpty()) {
            CommentCommand command = redoStack.pop();
            command.execute();
            userUndoStack.computeIfAbsent(userId, k -> new Stack<>()).push(command);
            addAuditLog(userId, "Redone: " + command.getDescription());
        }
    }

    public List<String> getAuditLog(Long userId) {
        return userAuditLog.getOrDefault(userId, new ArrayList<>());
    }

    private void addAuditLog(Long userId, String log) {
        userAuditLog.computeIfAbsent(userId, k -> new ArrayList<>()).add(log);
    }
}
