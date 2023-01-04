package com.example.supplychain;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
public class SupplyChain extends Application {
    public static final int width=700, height=600, headerBarSize=50;
    Pane bodyPane=new Pane();
    Login login=new Login();
    Button globalLoginButton;
    Button globalSignupButton;
    Label customerNameHeader=null;
    String customerEmail=null;
    public TableView<Product> cartTable;
    ProductDetails productDetails=new ProductDetails();
    Button addCartButton;
    Button buyButton;
    Button logoutButton;
    Button myCartButton;
    ObservableList<Product> cart;
    private GridPane headerBar() {
        Label logo=new Label("Gadgets Hub");
//        logo.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 19));
        logo.setStyle("-fx-text-fill: white; -fx-underline: true; -fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 18px;");
        logo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().addAll(productDetails.getAllProducts());
            }
        });

        TextField searchTextField=new TextField();
//        searchTextField.setId("textField");
        searchTextField.setPromptText("Enter search term");

        Button searchButton=new Button("Search");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String productName=searchTextField.getText();
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getProductsByName(productName));
            }
        });
        searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    String productName=searchTextField.getText();
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getProductsByName(productName));
                }
            }
        });
        globalLoginButton=new Button("Login");
        globalLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //clearing body
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
            }
        });

        globalSignupButton=new Button("Signup");
        globalSignupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //clearing body
                bodyPane.getChildren().clear();
//                bodyPane.getChildren().addAll(productDetails.getAllProducts());
                bodyPane.getChildren().add(signupPage());
            }
        });
        customerNameHeader =new Label("Welcome user!");
        customerNameHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane gridPane=new GridPane();
//        gridPane.setMinSize(bodyPane.getMinWidth(),headerBarSize-10);
        gridPane.setMinSize(bodyPane.getMinWidth(),headerBarSize);
        gridPane.setStyle("-fx-background-color: #5C5CFF");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(10);
//        Image image = new Image("/Users/rishavranjan/Desktop/Projects/Major/SupplyChain/images/logoImage.jpg");
//        gridPane.getChildren().add(new ImageView(image));
        gridPane.add(logo,0,0);
        gridPane.add(searchTextField,4,0);
        gridPane.add(searchButton,5,0);
        gridPane.add(globalLoginButton,8,0);
        gridPane.add(globalSignupButton,9,0);
        gridPane.add(customerNameHeader,10,0);
        return gridPane;
    }
    private GridPane loginPage(){
        Label emailLabel=new Label("Email:");
        Label passwordLabel=new Label("Password:");

        TextField emailTextField=new TextField();
        PasswordField passwordField=new PasswordField();
        Button loginButton=new Button("Login");
        Label messageLabel=new Label("Enter email and password.");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String email=emailTextField.getText();
                String password=passwordField.getText();
//                messageLabel.setText(email + " & " + password);
                if(login.customerLogin(email,password)==true){

                    //Database connection for displaying name
                    DatabaseConnection databaseConnection=new DatabaseConnection();
                    String query=String.format("select first_name from customer where email = '%s'",email);
                    ResultSet rs = databaseConnection.getQueryTable(query);
                    try {
                        while(rs.next()) {
                            customerNameHeader.setText("Welcome " + rs.getString("first_name"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    messageLabel.setText("Login successful!");
                    customerEmail=email;
                    globalLoginButton.setVisible(false);
                    globalSignupButton.setVisible(false);

                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getAllProducts());
                    buyButton.setVisible(true);
                    addCartButton.setVisible(true);
                    logoutButton.setVisible(true);
                    myCartButton.setVisible(true);
                }else{
                    messageLabel.setText("Incorrect email/password.");
                }
            }
        });

        //x and y coordinates
        GridPane gridPane=new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),bodyPane.getMinHeight());
//        gridPane.setStyle("-fx-background-color: #C0C0C0");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(emailLabel,0,0);
        gridPane.add(passwordLabel,0,1);
        gridPane.add(emailTextField,1,0);
        gridPane.add(passwordField,1,1);
        gridPane.add(loginButton,0,2);
        gridPane.add(messageLabel,1,2);
        return gridPane;
    }

    private GridPane signupPage() {
        Label firstName=new Label("First name:");
        Label lastName=new Label("Last name:");
        Label emailLabel=new Label("Email:");
        Label addressLabel=new Label("Address:");
        Label phoneLabel=new Label("Phone:");
        Label passwordLabel=new Label("Password:");

        TextField firstNameTextField=new TextField();
        firstNameTextField.setPromptText("Your first name");
        TextField lastNameTextField=new TextField();
        TextField phoneTextField=new TextField();
        phoneTextField.setPromptText("10 digit number");
        TextField emailTextField=new TextField();
        emailTextField.setPromptText("Enter your email");
        TextField addressTextField=new TextField();
        PasswordField passwordField=new PasswordField();

        addressTextField.setMinSize(100,60);

        Button signupButton=new Button("Signup");
        Label messageLabel=new Label("Create your account.");

        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(firstNameTextField.getText().length()>=3 && emailTextField.getText().length()>=8 && phoneTextField.getText().length()==10) {
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    String query = String.format("INSERT INTO customer (first_name,last_name,email,password,mobile,address) values('%s','%s','%s','%s','%s','%s')", firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordField.getText(), phoneTextField.getText(), addressTextField.getText());
//                ResultSet rs = databaseConnection.getQueryTable(query);
                    int rowCount = 0;
                    try {
                        rowCount = databaseConnection.executeUpdateQuery(query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (rowCount > 0) {
                        customerEmail = emailTextField.getText();
                        customerNameHeader.setText("Welcome " + firstNameTextField.getText());
                        globalLoginButton.setVisible(false);
                        globalSignupButton.setVisible(false);

                        bodyPane.getChildren().clear();
                        bodyPane.getChildren().add(productDetails.getAllProducts());
                        buyButton.setVisible(true);
                        addCartButton.setVisible(true);
                        logoutButton.setVisible(true);
                        myCartButton.setVisible(true);
                    } else if (rowCount == 0) {
                        messageLabel.setText("Sorry, we are not able to create your account");
                    }
                }else{
                    messageLabel.setText("Enter details properly");
                }
            }
        });

        GridPane gridPane=new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),bodyPane.getMinHeight());

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(firstName,0,0);
        gridPane.add(firstNameTextField,1,0);

        gridPane.add(lastName,0,1);
        gridPane.add(lastNameTextField,1,1);

        gridPane.add(emailLabel,0,2);
        gridPane.add(emailTextField,1,2);

        gridPane.add(passwordLabel,0,3);
        gridPane.add(passwordField,1,3);

        gridPane.add(phoneLabel,0,4);
        gridPane.add(phoneTextField,1,4);

        gridPane.add(addressLabel,0,5);
        gridPane.add(addressTextField,1,5);

        gridPane.add(signupButton,0,6);
        gridPane.add(messageLabel,1,6);
        return gridPane;
    }

    private GridPane footerBar() {
        addCartButton=new Button("Add to Cart");
        buyButton=new Button("Buy Now");
        myCartButton=new Button("My cart");
        buyButton.setVisible(false);
        addCartButton.setVisible(false);
        Label messageLabel=new Label("");
        cart=FXCollections.observableArrayList();
        addCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct=productDetails.getSelectedProduct();
                cart.add(selectedProduct);
                messageLabel.setText("Added to cart!");
            }
        });
        myCartButton.setVisible(false);
        myCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //clear body
                bodyPane.getChildren().clear();
                TableColumn id=new TableColumn("Id");
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                TableColumn name=new TableColumn("Name");
                name.setCellValueFactory(new PropertyValueFactory<>("name"));
                TableColumn price=new TableColumn("Price");
                price.setCellValueFactory(new PropertyValueFactory<>("price"));

                cartTable=new TableView<>();
                cartTable.setItems(cart);
                cartTable.getColumns().addAll(id,name,price);
                cartTable.setMinSize(SupplyChain.width,SupplyChain.height);
                cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                Pane tablePane=new Pane();
                tablePane.setMinSize(SupplyChain.width,SupplyChain.height);
                tablePane.getChildren().add(cartTable);

                bodyPane.getChildren().addAll(tablePane);

            }
        });

        logoutButton=new Button("Logout");
        logoutButton.setVisible(false);
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().addAll(productDetails.getAllProducts());
                customerNameHeader.setText("Logged out.");
                customerEmail=null;
                buyButton.setVisible(false);
                addCartButton.setVisible(false);
                logoutButton.setVisible(false);
                globalLoginButton.setVisible(true);
                globalSignupButton.setVisible(true);
                myCartButton.setVisible(false);
                messageLabel.setVisible(false);
            }
        });

        messageLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct=productDetails.getSelectedProduct();
                if(Order.placeOrder(customerEmail,selectedProduct)){
                    messageLabel.setText("Order placed!");
                }else{
                    messageLabel.setText("Order failed!");
                }
            }
        });
        GridPane gridPane=new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(),headerBarSize);
        gridPane.setStyle("-fx-background-color: #5C5CFF");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(headerBarSize+height);
        gridPane.setVgap(5);
        gridPane.setHgap(20);

        gridPane.add(myCartButton,0,0);
        gridPane.add(addCartButton,3,0);
        gridPane.add(buyButton,4,0);
        gridPane.add(messageLabel,5,0);
        gridPane.add(logoutButton,7,0);
        return gridPane;
    }
    private Pane createContent() {
        Pane root=new Pane();
        root.setPrefSize(width,height+2*headerBarSize);

        bodyPane.setTranslateY(headerBarSize);
        bodyPane.setMinSize(width,height);
        bodyPane.getChildren().addAll(productDetails.getAllProducts());

        root.getChildren().addAll(headerBar(),bodyPane,footerBar());
//        root.getChildren().addAll(headerBar(),signupPage(),footerBar());
        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("Gadgets Hub - Online tech shopping portal");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
    public static void main(String[] args) {
        launch();
    }
}
