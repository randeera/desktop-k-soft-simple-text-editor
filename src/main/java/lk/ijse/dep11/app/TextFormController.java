package lk.ijse.dep11.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep11.app.util.SearchResult;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public MenuItem menuItemAboutUs;
    public TextField txtFind;
    public Button btnDown;
    public Button btnUp;
    public CheckBox chkMatchCase;
    public Label lblResult;



    static int pos;
    private ArrayList<SearchResult> searchResults=new ArrayList<>();
    static int flag = 0;

//--------------------------------------------------
//--------------Initialize Method ------------------
//--------------------------------------------------

    public void initialize(){
        txtFind.textProperty().addListener((observableValue, s, current) -> {
            findResultCount();


        });
        txtBody.textProperty().addListener((observableValue, s, current) -> {
            findResultCount();

        });


//-------------------------------------
//--------------Find Result Count -----
//-------------------------------------

    }
    private void findResultCount() {
        String query = txtFind.getText();
        searchResults.clear();
        pos=0;
        txtBody.deselect();

        Pattern pattern;
        try {
            pattern = Pattern.compile(query,flag);
        } catch (Exception e) {
            return;
        }
        Matcher matcher = pattern.matcher(txtBody.getText());

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            SearchResult searchResult = new SearchResult(start, end);
            searchResults.add(searchResult);
        }
        lblResult.setText(String.format("%d Results", searchResults.size()));
        select();
    }

 //------------------------------------
//--------------New ------------------
//-------------------------------------

    public void menuItemNewOnAction(ActionEvent actionEvent) {
        if(isEdited) {
            ButtonType buttonNo = new ButtonType("No");
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the existing file before create new Text file",buttonNo,ButtonType.YES);
            Optional<ButtonType> button = confirm.showAndWait();
            if (button.isEmpty() || button.get() == ButtonType.NO) {

                return;
            }
            if (button.get() == ButtonType.YES) {
                menuItemSaveOnAction(actionEvent);
            }
        }
        AppInitializer.observableTitle.set("untitled");
        txtBody.setText("");
        isEdited = false;

    }
//-------------------------------------
//--------------Open ------------------
//-------------------------------------
    public void menuItemOpenOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open a text file");

        File file = fileChooser.showOpenDialog(txtBody.getScene().getWindow());

        if(file == null) return;

        fileAddress = file;
        String fileName = String.valueOf(file);
        AppInitializer.observableTitle.set(fileName.substring(fileName.lastIndexOf('/')+1));
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        String content = new String(bytes);
        txtBody.setText(new String(bytes));
    }
//-------------------------------------------
//--------------Save ------------------------
//-------------------------------------------
    public void menuItemSaveOnAction(ActionEvent actionEvent) {

        try {
            if(!isEdited) return;
            if (AppInitializer.observableTitle.getValue().equals("untitled")) {
                Alert inform = new Alert(Alert.AlertType.INFORMATION, "There is no text to save",ButtonType.CLOSE);
                inform.show();
                return;
            }
            if (AppInitializer.observableTitle.getValue().equals("*untitled")) {
                menuItemSaveAsOnAction(actionEvent);


            } else {
                FileOutputStream fos = new FileOutputStream(fileAddress, false);

                String text = txtBody.getText();
                byte[] bytes = text.getBytes();
                fos.write(bytes);
                fos.close();
                isEdited = false;
                AppInitializer.observableTitle.set(AppInitializer.observableTitle.get().substring(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
//----------------------------------------
//--------------Save As ------------------
//----------------------------------------
    public void menuItemSaveAsOnAction(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save a text File");

            File file =fileChooser.showSaveDialog(txtBody.getScene().getWindow());
            if(file == null)return;
            fileAddress =file;
            FileOutputStream fos = new FileOutputStream(file,false);

            String text = txtBody.getText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();
            String fileName = String.valueOf(file);
            AppInitializer.observableTitle.set(fileName.substring(fileName.lastIndexOf('/')+1));
            isEdited = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//---------------------------------------
//--------------check is Edited ----------
//----------------------------------------
    public void txtBodyOnKeyReleased() {
        isEdited = true;
        if (AppInitializer.observableTitle.get().charAt(0) == '*')return;
        AppInitializer.observableTitle.set("*".concat(AppInitializer.observableTitle.get()));
    }

//---------------------------------------
//--------------close -------------------
//----------------------------------------

    public void menuItemCloseOnAction(ActionEvent actionEvent) {
        if (isEdited) {

            ButtonType buttonSaveClose = new ButtonType("Save and Close");
            ButtonType buttonClose = new ButtonType("Close without Save");

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, String.format("Save changers to the Document %s before Closing", AppInitializer.observableTitle)
                    ,ButtonType.CANCEL,buttonSaveClose,buttonClose);
            Optional<ButtonType> button =confirm.showAndWait();
            if(button.isEmpty() || button.get() == ButtonType.CANCEL) return;
            if (button.get() == buttonSaveClose) {
                menuItemSaveOnAction(actionEvent);
                if(isEdited) return;
            }
        }
        Platform.exit();
    }
//----------------------------------------
//--------------About Us -----------------
//----------------------------------------
    public void menuItemAboutUsOnAction(ActionEvent actionEvent) throws IOException {
        URL file = getClass().getResource("/view/AboutUs.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(file);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("About K-Soft Text Editor");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }


//----------------------------------------
//--------------btn Down -----------------
//----------------------------------------
    public void btnDownOnAction(ActionEvent actionEvent) {
        pos++;
        if (pos == searchResults.size()) {
            pos=-1;
            return;
        }
        select();
    }
    private void select() {
        if(searchResults.isEmpty())return;
        SearchResult searchResult = searchResults.get(pos);
        txtBody.selectRange(searchResult.getStart(),searchResult.getEnd());
        lblResult.setText(String.format("%d / %d Results", pos+1,searchResults.size()));
    }

//----------------------------------------
//--------------btn Up -------------------
//----------------------------------------
    public void btnUpOnAction(ActionEvent actionEvent) {
        pos--;
        if (pos < 0) {
            pos=searchResults.size();
            return;
        }
        select();
    }
//----------------------------------------
//--------------check Match---------------
//----------------------------------------
    public void chkMatchCaseOnAction(ActionEvent actionEvent) {
        flag = flag == 0 ? 2 : 0;
        findResultCount();
    }

    public void rootOnDragDropped(DragEvent dragEvent) throws IOException {
        File file =dragEvent.getDragboard().getFiles().get(0);
        AppInitializer.observableTitle.setValue(file.getName());
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();

        fis.close();
        txtBody.setText(new String(bytes));
        txtBodyOnKeyReleased();
    }

    public void rootOnDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.ANY);    //Drag accepted
    }

    public void txtBodyOnMouseClicked(MouseEvent mouseEvent) {
        txtBodyOnKeyReleased();
    }
}
