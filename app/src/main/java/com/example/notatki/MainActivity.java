package com.example.notatki;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainLinearLayout;
    private EditText noteInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainLinearLayout = findViewById(R.id.lay);
        noteInput = findViewById(R.id.noteInput);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = noteInput.getText().toString().trim();
                if (!noteText.isEmpty()) {
                    createNewNote(noteText);
                    noteInput.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a note!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void createNewNote(String noteText) {
        LinearLayout newNoteLayout = new LinearLayout(this);
        newNoteLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newNoteLayout.setOrientation(LinearLayout.HORIZONTAL);
        newNoteLayout.setPadding(8, 8, 8, 8);

        TextView newNoteTextView = new TextView(this);
        newNoteTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));  // Weight=1 to make it occupy available space
        newNoteTextView.setText(noteText);
        newNoteTextView.setTextSize(18);
        newNoteTextView.setPadding(8, 8, 8, 8);

        Button editButton = new Button(this);
        editButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editButton.setText("Edit");
        editButton.setPadding(8, 8, 8, 8);

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deleteButton.setText("Delete");
        deleteButton.setPadding(8, 8, 8, 8);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(newNoteTextView);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(newNoteLayout);
            }
        });

        newNoteLayout.addView(newNoteTextView);
        newNoteLayout.addView(editButton);
        newNoteLayout.addView(deleteButton);

        mainLinearLayout.addView(newNoteLayout);

    }
    private void showEditDialog(TextView noteTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_text, null);
        builder.setView(dialogView);

        EditText editNoteInput = dialogView.findViewById(R.id.editNoteInput);
        editNoteInput.setText(noteTextView.getText());  // Set the current note text

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the note with the new text
                noteTextView.setText(editNoteInput.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
    private void showDeleteConfirmationDialog(LinearLayout noteLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Do you want to delete the note?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainLinearLayout.removeView(noteLayout);  // Remove the note layout
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}