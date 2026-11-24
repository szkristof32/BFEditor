# User manual

## Opening and editing files

Use **File > New** to create a new unsaved file in the editor. Use **File > Open** to select and open an existing Brainfuck source file in a new tab. An asterisk (*) next to a tab name indicates that the file has unsaved changes.

To save the active file, use **File > Save**. If the file has not been saved before, a dialog lets you choose its location. **File > Save As** always opens this dialog. Use **File > Exit** to close the application; you are promted if you want to continue if there are unsaved changes. If yes all these changes are lost.

The **Edit** menu provides Undo, Redo, Cut, Copy, and Paste commands for the active editor tab. Select text before using Cut or Copy, and ensure there is text on the clipboard before using Paste.

## Running a program

Open or create a Brainfuck source file, then use **Run > Execute** or the corresponding button in the run toolbar. Characters other than the eight Brainfuck instructions are treated as comments.

Program output appears in the virtual console. For an input instruction (`,`), enter a character in the virtual console.

Use **Run > Debug** to start step-by-step execution. While the program is paused, use **Run > Step** or the step button to execute the next instruction. The runtime state shows the memory cells and instruction pointer. Its memory view updates as execution proceeds.

## Preferences

Use **Edit > Preferences** to configure the interpreter:

- **Memory size:** number of memory cells allocated at startup.
- **Memory cell overflow:** behavior when a cell value exceeds its defined range.
- **Initial cell value:** value assigned to each memory cell before execution.

Click **Save** to apply and store the settings, or **Cancel** to discard changes.

## Brainfuck instruction reference

- `>` moves the data pointer to the next cell.
- `<` moves it to the previous cell.
- `+` increments the current cell modulo 256.
- `-` decrements the current cell modulo 256.
- `.` outputs the current cell value as a character.
- `,` reads one character as input into the current cell.
- `[` jumps after the matching `]` when the current cell is zero.
- `]` jumps back to the matching `[` when the current cell is not zero.
