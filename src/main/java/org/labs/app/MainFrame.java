package org.labs.app;

import org.labs.model.Employee;
import org.labs.service.ContractService;
import org.labs.service.EmployeeService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class MainFrame extends JFrame {
    private Panel mainPanel = new Panel();
    private JButton buttonEnter = new JButton("Войти");
    private JButton buttonRegister = new JButton("Зарегистрироваться");
    private JTextField loginTextField = new JTextField();
    private JTextField passTextField = new JPasswordField();
    private JLabel loginLabel = new JLabel();
    private JLabel passLabel = new JLabel();
    private JLabel errorLabel = new JLabel();

    @Autowired
    ContractService contractService;

    @Autowired
    EmployeeService employeeService;

    static final Color AQUA = new Color(0, 200, 255);
    static final Font font = new Font("Verdana", Font.PLAIN, 12);

    public MainFrame() {
        createFrameAndPanel();
        createStartButtons();
        createStartTextFieldsAndLabels();
    }

    private void createStartTextFieldsAndLabels() {
        errorLabel.setText("Неверный логин или пароль");
        errorLabel.setBounds(215, 350, 200, 30);
        errorLabel.setFont(font);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);
        loginLabel.setText("Логин:");
        loginLabel.setBounds(215, 170, 100, 30);
        loginLabel.setFont(font);
        mainPanel.add(loginLabel);
        loginTextField.setBounds(215, 200, 170, 30);
        mainPanel.add(loginTextField);
        passLabel.setText("Пароль:");
        passLabel.setBounds(215, 270, 100, 30);
        passLabel.setFont(font);
        mainPanel.add(passLabel);
        passTextField.setBounds(215, 300, 170, 30);
        mainPanel.add(passTextField);
    }

    private void createStartButtons() {
        buttonEnter.setBounds(215, 400, 170, 50);
        buttonEnter.addActionListener(e -> enterAuth());
        buttonEnter.setForeground(Color.BLACK);
        buttonEnter.setBackground(AQUA);
        buttonEnter.setFocusable(false);
        mainPanel.add(buttonEnter);


        buttonRegister.setBounds(215, 500, 170, 50);
        buttonRegister.addActionListener(e -> createUser());
        buttonRegister.setForeground(Color.BLACK);
        buttonRegister.setBackground(AQUA);
        buttonRegister.setFocusable(false);
        mainPanel.add(buttonRegister);
    }

    private void createUser() {
        mainPanel.removeAll();

        mainPanel.add(createLabel("Регистрация", 250, 30, 200, 30));
        mainPanel.add(createLabel("Имя:", 30, 65, 200, 30));
        mainPanel.add(createLabel("Фамилия:", 30, 115, 200, 30));
        mainPanel.add(createLabel("Отчество:", 30, 165, 200, 30));
        mainPanel.add(createLabel("Телефон:", 30, 215, 200, 30));
        mainPanel.add(createLabel("email:", 30, 265, 200, 30));
        mainPanel.add(createLabel("fax:", 30, 315, 200, 30));
        mainPanel.add(createLabel("Логин:", 30, 365, 200, 30));
        mainPanel.add(createLabel("Пароль:", 30, 415, 200, 30));
        mainPanel.add(createLabel("Ваша роль (Рядовой, Менеджер, Админ):", 30, 465, 270, 30));

        List<JTextField> listTextFields = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            listTextFields.add(createTextField(30, 90 + (i * 50), 200, 30));
            mainPanel.add(listTextFields.get(i));
        }

        JButton submitButt = new JButton("Зарегистрироваться");
        submitButt.setBounds(315, 480, 170, 50);
        submitButt.addActionListener(e -> registerUser(listTextFields));
        submitButt.setForeground(Color.BLACK);
        submitButt.setBackground(AQUA);
        submitButt.setFocusable(false);
        mainPanel.add(submitButt);

        JButton backButt = new JButton("Назад");
        backButt.setBounds(500, 480, 70, 50);
        backButt.addActionListener(e -> backToStartFrame());
        backButt.setForeground(Color.BLACK);
        backButt.setBackground(AQUA);
        backButt.setFocusable(false);
        mainPanel.add(backButt);

        mainPanel.repaint();
    }

    private void backToStartFrame() {
        mainPanel.removeAll();
        createStartButtons();
        createStartTextFieldsAndLabels();
        mainPanel.repaint();
    }

    private void registerUser(List<JTextField> jTextFieldList) {
        Employee employee = new Employee();
        employee.setFirstName(jTextFieldList.get(0).getText());
        employee.setLastName(jTextFieldList.get(1).getText());
        employee.setMiddleName(jTextFieldList.get(2).getText());
        employee.setPhone(jTextFieldList.get(3).getText());
        employee.setEmail(jTextFieldList.get(4).getText());
        employee.setFax(jTextFieldList.get(5).getText());
        employee.setLogin(jTextFieldList.get(6).getText());
        employee.setPassword(BCrypt.hashpw(jTextFieldList.get(7).getText(), BCrypt.gensalt()));
        String positionId = jTextFieldList.get(8).getText();
        if (positionId.equals("Рядовой")) {
            employee.setPositionId(2);
        } else if (positionId.equals("Менеджер")) {
            employee.setPositionId(1);
        } else if (positionId.equals("Админ")) {
            employee.setPositionId(3);
        }
        try {
            employeeService.save(employee);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Что то не так!");
            backToStartFrame();
        }
        JOptionPane.showMessageDialog(this, "Вы зарегистрированы!");
        backToStartFrame();
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        return label;
    }

    private void enterAuth() {
        Employee employee = new Employee();
        employee.setLogin(loginTextField.getText());
        Employee currentUser = employeeService.selectEmployeeByLogin(employee);
        if (Objects.nonNull(currentUser) && BCrypt.checkpw(passTextField.getText(), currentUser.getPassword())) {
            mainPanel.remove(buttonEnter);
            mainPanel.remove(buttonRegister);
            //mainPanel.remove(loginLabel);
            mainPanel.remove(loginTextField);
            mainPanel.remove(passLabel);
            mainPanel.remove(passTextField);
            mainPanel.remove(errorLabel);
            loginLabel.setText("Вы зашли!");
        } else {
            errorLabel.setVisible(true);
        }
        mainPanel.repaint();
    }

    private void createFrameAndPanel() {
        setTitle("GUI for DB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 200, 600, 600);
        add(mainPanel);
        setContentPane(mainPanel);
        setVisible(true);
        mainPanel.setLayout(null);
    }


}
