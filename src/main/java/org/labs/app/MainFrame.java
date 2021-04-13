package org.labs.app;

import org.labs.model.Contract;
import org.labs.model.Employee;
import org.labs.service.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
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
    /*1 = Менеджер, 2 = Рядовой, 3 = Админ*/
    private int position = 3;

    @Autowired
    ContractService contractService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    MissionService missionService;

    @Autowired
    PositionService positionService;

    @Autowired
    PriorityService priorityService;

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
            mainPanel.removeAll();
            createMainMenu();
            mainPanel.repaint();
        } else {
            errorLabel.setVisible(true);
        }
        mainPanel.repaint();
    }

    private void createMainMenu() {
        JButton showContractsButt = new JButton("Информация о контрактах");
        showContractsButt.setBounds(30, 100, 250, 50);
        showContractsButt.addActionListener(e -> showContractsTable());
        showContractsButt.setForeground(Color.BLACK);
        showContractsButt.setBackground(AQUA);
        showContractsButt.setFocusable(false);
        mainPanel.add(showContractsButt);
        JButton showCustomersButt = new JButton("Информация о клиентах");
        showCustomersButt.setBounds(320, 100, 250, 50);
        showCustomersButt.addActionListener(e -> backToStartFrame());
        showCustomersButt.setForeground(Color.BLACK);
        showCustomersButt.setBackground(AQUA);
        showCustomersButt.setFocusable(false);
        mainPanel.add(showCustomersButt);
        JButton showEmployeesButt = new JButton("Информация о сотрудниках");
        showEmployeesButt.setBounds(30, 200, 250, 50);
        showEmployeesButt.addActionListener(e -> backToStartFrame());
        showEmployeesButt.setForeground(Color.BLACK);
        showEmployeesButt.setBackground(AQUA);
        showEmployeesButt.setFocusable(false);
        mainPanel.add(showEmployeesButt);
        JButton showEquipmentsButt = new JButton("Информация об оборудовании");
        showEquipmentsButt.setBounds(320, 200, 250, 50);
        showEquipmentsButt.addActionListener(e -> backToStartFrame());
        showEquipmentsButt.setForeground(Color.BLACK);
        showEquipmentsButt.setBackground(AQUA);
        showEquipmentsButt.setFocusable(false);
        mainPanel.add(showEquipmentsButt);
        JButton showMissionsButt = new JButton("Информация о заданиях");
        showMissionsButt.setBounds(30, 300, 250, 50);
        showMissionsButt.addActionListener(e -> backToStartFrame());
        showMissionsButt.setForeground(Color.BLACK);
        showMissionsButt.setBackground(AQUA);
        showMissionsButt.setFocusable(false);
        mainPanel.add(showMissionsButt);
        JButton showPositionsButt = new JButton("Информация о ролях в бд");
        showPositionsButt.setBounds(320, 300, 250, 50);
        showPositionsButt.addActionListener(e -> backToStartFrame());
        showPositionsButt.setForeground(Color.BLACK);
        showPositionsButt.setBackground(AQUA);
        showPositionsButt.setFocusable(false);
        mainPanel.add(showPositionsButt);
        JButton showPrioriesButt = new JButton("Информация о приоритетах заданий");
        showPrioriesButt.setBounds(30, 400, 250, 50);
        showPrioriesButt.addActionListener(e -> backToStartFrame());
        showPrioriesButt.setForeground(Color.BLACK);
        showPrioriesButt.setBackground(AQUA);
        showPrioriesButt.setFocusable(false);
        mainPanel.add(showPrioriesButt);
        JButton backButt = new JButton("Выйти из аккаунта");
        backButt.setBounds(320, 400, 250, 50);
        backButt.addActionListener(e -> backToStartFrame());
        backButt.setForeground(Color.BLACK);
        backButt.setBackground(AQUA);
        backButt.setFocusable(false);
        mainPanel.add(backButt);
    }

    private void showContractsTable() {
        JFrame contractFrame = new JFrame();
        contractFrame.setTitle("Contracts");
        contractFrame.setBounds(400,400, 800,500);
        contractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel contractPanel = new Panel();
        contractFrame.add(contractPanel);
        contractFrame.setContentPane(contractPanel);
        contractFrame.setVisible(true);
        List<Contract> list = contractService.selectAllContracts();
        Object[][] array = new String[list.size()][4];
        Object[] columnsHeader = new String[] {"Number", "DateConclusion",
                "Description", "CustomerId"};
        for (int i = 0; i < list.size(); i++) {
            Contract tmp = list.get(i);
            array[i][0] = tmp.getNumber().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            array[i][1] = formatter.format(tmp.getDateConclusion());
            array[i][2] = tmp.getDescription();
            array[i][3] = tmp.getCustomerId().toString();
        }
        JTable table = new JTable(array, columnsHeader);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(100);
        //table.setBounds(5,5, 790,490);
        table.setRowHeight(30);
        //table.setIntercellSpacing(new Dimension(10, 10));
        //JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setSize(790,490);
        //contractPanel.add(scrollPane);
        //table.add(scrollPane);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(780,480));
        contents.add(new JScrollPane(table));
        //contents.add(new JScrollPane());
        contractPanel.add(contents);
        //contractPanel.add(table);
        contractPanel.repaint();
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
