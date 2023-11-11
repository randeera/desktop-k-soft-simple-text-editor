package lk.ijse.dep11.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.*;


public class TextFormController {

    public AnchorPane root;
    public HTMLEditor htmlEditor;
    public MenuBar menuBar;
    public Menu menuFile;
    public MenuItem menuItemNew;
    public MenuItem menuItemOpen;
    public MenuItem menuItemSave;
    public MenuItem menuItemSaveAs;
    public MenuItem menuItemClose;
    public Menu menuHelp;
    public MenuItem menuItemAbout;
    public Label lblFooter;

    public File fileAddress;
    public TextArea txtBody;
    private static boolean isEdited = false;

    public void menuItemNewOnAction(ActionEvent actionEvent) {


    }

    public void menuItemOpenOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a New File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files" , "*.txt", "*.doc", "*.pages", "*.docx", "*.md", "*.wps") );
        File file = fileChooser.showOpenDialog(txtBody.getScene().getWindow());
        String fileLocation = file.getAbsolutePath();
        String fileName = String.valueOf(file);
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
            txtBody.setText(sb.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuItemSaveOnAction(ActionEvent actionEvent) {

        if(!isEdited) return;
        if (AppInitializer.observableTitle.getValue().equals("untitled")) {
            Alert inform = new Alert(Alert.AlertType.INFORMATION, "There is no text to save",ButtonType.CLOSE);
            inform.show();
            return;
        }
        if (AppInitializer.observableTitle.getValue().equals("*untitled")) {
            menuItemSaveAsOnAction(actionEvent);


        } else {
            try {
                FileOutputStream fos = new FileOutputStream(fileAddress, false);

                String text = txtBody.getText();
                byte[] bytes = text.getBytes();
                fos.write(bytes);
                fos.close();
                isEdited = false;
                AppInitializer.observableTitle.set(AppInitializer.observableTitle.get().substring(1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void menuItemSaveAsOnAction(ActionEvent actionEvent) {
    }
}
