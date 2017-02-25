package paint.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import paint.model.ArrayStack;

public class PaintController {
	@FXML
	private ColorPicker colorChoice;
	@FXML
	private ToggleGroup	drawingTool = new ToggleGroup();
	@FXML
	private RadioButton penButton;
	@FXML
	private RadioButton lineButton;
	@FXML
	private RadioButton circleButton;
	@FXML
	private RadioButton rectangleButton;
	@FXML
	private RadioButton fillButton;
	@FXML
	private Pane board;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuExit;
	@FXML
	private MenuItem menuAbout;
	@FXML
	private MenuItem menuUndo;
	@FXML
	private MenuItem menuRedo;
	@FXML
	private VBox selectionStuff;
	@FXML
	private MenuBar menuStuff;
	@FXML
	private Slider strokeSlider;

	private Color currentColor = Color.BLACK;
	private URL currentPath;
	private FileChooser fileChooser = new FileChooser();
	private FileChooser fileSaver = new FileChooser();
	private String selectedTool = "Pen";
	private double sx;
	private double sy;
	private double ex;
	private double ey;
	private ArrayStack<Object> undoStack = new ArrayStack<Object>();
	private ArrayStack<Object> redoStack = new ArrayStack<Object>();
	private Rectangle temprec;
	private Circle tempcircle;
	private Line templine;
	
	public void initialize() {
		colorChoice.setValue(Color.BLACK);
		board.setOnMousePressed(event -> startDrag(event));
		board.setOnMouseReleased(event -> endDrag(event));
		board.setOnMouseDragged(event -> draw(event));
		board.setOnMouseClicked(event -> fill(event));
		colorChoice.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentColor = colorChoice.getValue();
			}
		});
		board.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		board.setLayoutX(selectionStuff.getWidth());
		board.setLayoutY(menuStuff.getHeight());
		fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
		menuOpen.setOnAction(event -> {
			File fileToOpen = fileChooser.showOpenDialog(menuStuff.getScene().getWindow());
			if (fileToOpen != null) {
				try {
					// Based on http://stackoverflow.com/a/6098518
					currentPath = fileToOpen.toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				openFile();
			}
		});	
		menuSave.setOnAction(event -> {
			File fileToSave = fileSaver.showSaveDialog(menuStuff.getScene().getWindow());
			if (fileToSave != null) {
				try {
					currentPath = fileToSave.toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				saveFile(fileToSave);
			}
		});
		menuExit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
		// Based on http://stackoverflow.com/a/22167142
		menuAbout.setOnAction(event -> {
			try {
				Stage dialog = new Stage();
	            dialog.initModality(Modality.APPLICATION_MODAL);
	            dialog.initOwner(menuStuff.getScene().getWindow());
				BorderPane aboutWindow = (BorderPane)FXMLLoader.load(getClass().getResource("PaintAboutGUI.fxml"));
				Scene scene = new Scene(aboutWindow);
				dialog.setScene(scene);
				dialog.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		// Based on http://stackoverflow.com/a/21374681
		drawingTool.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (drawingTool.getSelectedToggle() != null) {
					RadioButton selectedToolButton = (RadioButton)new_toggle.getToggleGroup().getSelectedToggle();
					selectedTool = selectedToolButton.getText();
				}
			}
		});
	}

	
	public void startDrag(MouseEvent event) {
		sx = event.getX();
		sy = event.getY();
	}

	public void endDrag(MouseEvent event) {
		ex = event.getX();
		ey = event.getY();
		board.getChildren().remove(tempcircle);
		board.getChildren().remove(templine);
		board.getChildren().remove(temprec);
		if (selectedTool.equals("Line")) {
			Line line = new Line(sx, sy, ex, ey);
			line.setStroke(currentColor);
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			line.setStrokeWidth(strokeSlider.getValue());
			board.getChildren().add(line);
			undoStack.push(line);
			if (!redoStack.isEmpty()) {
				redoStack = new ArrayStack<Object>();
			}
		} else if (selectedTool.equals("Circle")) {
			Circle circle = new Circle(sx, sy, Math.sqrt(Math.pow((sx - ex), 2) + Math.pow((sy - ey), 2)));
			circle.setStroke(currentColor);
			circle.setFill(Color.TRANSPARENT);
			circle.setStrokeLineCap(StrokeLineCap.ROUND);
			circle.setStrokeWidth(strokeSlider.getValue());
			board.getChildren().add(circle);
			undoStack.push(circle);
			if (!redoStack.isEmpty()) {
				redoStack = new ArrayStack<Object>();
			}
		} else if (selectedTool.equals("Rectangle")) {
			Rectangle rec;
			if (sx < ex && sy < ey) {
	            rec = new Rectangle(sx, sy, Math.abs(sx - ex), Math.abs(sy - ey));
			} else if (sx > ex && sy < ey) {
	            rec = new Rectangle(ex, sy, Math.abs(ex - sx), Math.abs(sy - ey));
	        } else if (sx < ex && sy > ey) {
	            rec = new Rectangle(sx, ey, Math.abs(sx - ex), Math.abs(ey - sy));
	        } else {
	            rec = new Rectangle(ex, ey, Math.abs(ex - sx), Math.abs(ey - sy));
	        }
			rec.setStroke(currentColor);
			rec.setFill(Color.TRANSPARENT);
			rec.setStrokeLineCap(StrokeLineCap.ROUND);
			rec.setStrokeWidth(strokeSlider.getValue());
			board.getChildren().add(rec);
			undoStack.push(rec);
			if (!redoStack.isEmpty()) {
				redoStack = new ArrayStack<Object>();
			}
		}
	}
	
	public void draw(MouseEvent event) {
		double fx = event.getX();
		double fy = event.getY();
		if (selectedTool.equals("Pen")) {
			Line line = new Line(sx, sy, fx, fy);
			line.setStroke(currentColor);
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			line.setStrokeWidth(strokeSlider.getValue());
			board.getChildren().add(line);
			undoStack.push(line);
			if (!redoStack.isEmpty()) {
				redoStack = new ArrayStack<Object>();
			}
			sx = fx;
			sy = fy;
		} else {
			if (selectedTool.equals("Line")) {
				Line line = new Line(sx, sy, fx, fy);
				line.setStroke(currentColor);
				line.setStrokeLineCap(StrokeLineCap.ROUND);
				line.setStrokeWidth(strokeSlider.getValue());
				board.getChildren().remove(templine);
				board.getChildren().add(line);
				templine = line;
			} else if (selectedTool.equals("Circle")) {
				Circle circle = new Circle(sx, sy, Math.sqrt(Math.pow((sx - fx), 2) + Math.pow((sy - fy), 2)));
				circle.setStroke(currentColor);
				circle.setFill(Color.TRANSPARENT);
				circle.setStrokeLineCap(StrokeLineCap.ROUND);
				circle.setStrokeWidth(strokeSlider.getValue());
				board.getChildren().remove(tempcircle);
				board.getChildren().add(circle);
				tempcircle = circle;
			} else if (selectedTool.equals("Rectangle")) {
				Rectangle rec;
				if (sx < fx && sy < fy) {
		            rec = new Rectangle(sx, sy, Math.abs(sx - fx), Math.abs(sy - fy));
				} else if (sx > fx && sy < fy) {
		            rec = new Rectangle(fx, sy, Math.abs(fx - sx), Math.abs(sy - fy));
		        } else if (sx < fx && sy > fy) {
		            rec = new Rectangle(sx, fy, Math.abs(sx - fx), Math.abs(fy - sy));
		        } else {
		            rec = new Rectangle(fx, fy, Math.abs(fx - sx), Math.abs(fy - sy));
		        }
				rec.setStroke(currentColor);
				rec.setFill(Color.TRANSPARENT);
				rec.setStrokeLineCap(StrokeLineCap.ROUND);
				rec.setStrokeWidth(strokeSlider.getValue());
				board.getChildren().remove(temprec);
				board.getChildren().add(rec);
				temprec = rec;
			}	
		}
	}
	
	private void fill(MouseEvent event) {
		if (selectedTool.equals("Fill")) {
			try {
				Shape objectSelected = ((Shape)((Node)event.getTarget()));
				objectSelected.setFill(currentColor);
			} catch (ClassCastException e) {
				board.setBackground(new Background(new BackgroundFill(currentColor, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}
	}

	// Code adapted from http://code.makery.ch/blog/javafx-2-snapshot-as-png-image/
	private void saveFile(File fileToSave) {
		WritableImage savedPane = board.snapshot(new SnapshotParameters(), null);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(savedPane, null), "png", fileToSave);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openFile() {
		board.setBackground(new Background(new BackgroundImage(new Image(currentPath.toString()), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
	}
	
	@FXML
	private void undo() {
		if (!undoStack.isEmpty()) {
			Object undoObject = undoStack.pop();
			board.getChildren().remove(undoObject);
			redoStack.push(undoObject);
		}
	}
	
	@FXML
	private void redo() {
		if (!redoStack.isEmpty()) {
			Object redoObject = redoStack.pop();
			board.getChildren().add((Node)redoObject);
			undoStack.push(redoObject);
		}
	}
}
