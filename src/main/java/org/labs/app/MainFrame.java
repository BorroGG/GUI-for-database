package org.labs.app;

import org.labs.model.*;
import org.labs.service.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Component
public class MainFrame extends JFrame {
    private final List<JFrame> activeFrames = new LinkedList<>();
    private final Panel mainPanel = new Panel();
    private Employee currentUser;

    private final ContractService contractService;

    private final EmployeeService employeeService;

    private final CustomerService customerService;

    private final EquipmentService equipmentService;

    private final MissionService missionService;

    private final PositionService positionService;

    private final PriorityService priorityService;

    private static final Color AQUA = new Color(0, 200, 255);
    private static final Color SOFT_PINK = new Color(217, 113, 113);
    private static final Color RED = new Color(255, 0, 0);
    private static final Color GREEN = new Color(76, 230, 140);
    private static final Font COMMON_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Font BOLD_FONT = new Font("Verdana", Font.BOLD, 12);

    @Autowired
    public MainFrame(ContractService contractService, EmployeeService employeeService,
                     CustomerService customerService, EquipmentService equipmentService,
                     MissionService missionService, PositionService positionService,
                     PriorityService priorityService) {
        this.contractService = contractService;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.equipmentService = equipmentService;
        this.missionService = missionService;
        this.positionService = positionService;
        this.priorityService = priorityService;

        JTextField loginTextField = new JTextField();
        JTextField passTextField = new JPasswordField();
        JLabel errorLabel = new JLabel();
        createFrameAndPanel();
        createStartTextFieldsAndLabels(loginTextField, passTextField, errorLabel);
        createStartButtons(loginTextField, passTextField, errorLabel);
    }

    private void createStartTextFieldsAndLabels(JTextField loginTextField,
                                                JTextField passTextField,
                                                JLabel errorLabel) {
        errorLabel.setText("Неверный логин или пароль");
        errorLabel.setBounds(215, 350, 200, 30);
        errorLabel.setFont(COMMON_FONT);
        errorLabel.setForeground(RED);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);

        JLabel loginLabel = new JLabel();
        loginLabel.setText("Логин:");
        loginLabel.setBounds(215, 170, 100, 30);
        loginLabel.setFont(COMMON_FONT);
        mainPanel.add(loginLabel);

        loginTextField.setBounds(215, 200, 170, 30);
        mainPanel.add(loginTextField);

        JLabel passLabel = new JLabel();
        passLabel.setText("Пароль:");
        passLabel.setBounds(215, 270, 100, 30);
        passLabel.setFont(COMMON_FONT);
        mainPanel.add(passLabel);

        passTextField.setBounds(215, 300, 170, 30);
        mainPanel.add(passTextField);
    }

    private void createStartButtons(JTextField loginTextField,
                                    JTextField passTextField,
                                    JLabel errorLabel) {
        JButton buttonEnter = new JButton("Войти");
        buttonEnter.setBounds(215, 400, 170, 50);
        buttonEnter.addActionListener(e -> enterAuth(loginTextField, passTextField, errorLabel));
        buttonEnter.setForeground(Color.BLACK);
        buttonEnter.setBackground(AQUA);
        buttonEnter.setFocusable(false);
        mainPanel.add(buttonEnter);

        JButton buttonRegister = new JButton("Зарегистрироваться");
        buttonRegister.setBounds(215, 500, 170, 50);
        buttonRegister.addActionListener(e -> createUser());
        buttonRegister.setForeground(Color.BLACK);
        buttonRegister.setBackground(AQUA);
        buttonRegister.setFocusable(false);
        mainPanel.add(buttonRegister);
    }

    private void createUser() {
        mainPanel.removeAll();

        mainPanel.add(createLabel("Регистрация", 250, 30, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Имя:", 30, 65, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Фамилия:", 30, 115, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Отчество:", 30, 165, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Телефон:", 30, 215, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("email:", 30, 265, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("fax:", 30, 315, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Логин:", 30, 365, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Пароль:", 30, 415, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Ваша роль:", 30, 465, 270, 30, COMMON_FONT));

        DefaultComboBoxModel<String> cbModelForRole = new DefaultComboBoxModel<>(new String[]{
                PositionInDB.RYADOVOI.getPosition(),
                PositionInDB.MANAGER.getPosition(),
                PositionInDB.ADMIN.getPosition()});
        JComboBox<String> comboBoxForRole = new JComboBox<>(cbModelForRole);

        List<Object> panelFields = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            if (i != 8) {
                panelFields.add(createTextField(90 + (i * 50), 200));
                mainPanel.add((JTextField) panelFields.get(i));
            } else {
                comboBoxForRole.setBounds(30, 90 + (i * 50), 200, 30);
                panelFields.add(comboBoxForRole);
                mainPanel.add(comboBoxForRole);
            }
        }

        JButton registerButt = new JButton("Зарегистрироваться");
        registerButt.setBounds(315, 480, 170, 50);
        registerButt.addActionListener(e -> registerUser(panelFields));
        registerButt.setForeground(Color.BLACK);
        registerButt.setBackground(AQUA);
        registerButt.setFocusable(false);
        mainPanel.add(registerButt);

        JButton backButt = new JButton("Назад");
        backButt.setBounds(500, 480, 70, 50);
        backButt.addActionListener(e -> backToStartFrame());
        backButt.setForeground(Color.BLACK);
        backButt.setBackground(SOFT_PINK);
        backButt.setFocusable(false);
        mainPanel.add(backButt);

        mainPanel.repaint();
    }

    private void backToStartFrame() {
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.clear();
        currentUser = null;
        mainPanel.removeAll();
        JTextField loginTextField = new JTextField();
        JTextField passTextField = new JPasswordField();
        JLabel errorLabel = new JLabel();
        createStartTextFieldsAndLabels(loginTextField, passTextField, errorLabel);
        createStartButtons(loginTextField, passTextField, errorLabel);
        mainPanel.repaint();
    }

    private void registerUser(List<Object> panelFields) {
        String[] valuesInFields = new String[9];
        for (int i = 0; i < 9; i++) {
            if (i != 8) {
                valuesInFields[i] = ((JTextField) panelFields.get(i)).getText();
            } else {
                valuesInFields[i] = ((JComboBox) panelFields.get(i)).getSelectedItem().toString();
            }
            if (valuesInFields[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все данные!");
                return;
            }
        }
        Employee employee = new Employee();
        employee.setFirstName(valuesInFields[0]);
        employee.setLastName(valuesInFields[1]);
        employee.setMiddleName(valuesInFields[2]);
        employee.setPhone(valuesInFields[3]);
        employee.setEmail(valuesInFields[4]);
        employee.setFax(valuesInFields[5]);
        employee.setLogin(valuesInFields[6]);
        employee.setPassword(BCrypt.hashpw(valuesInFields[7], BCrypt.gensalt()));
        if (valuesInFields[8].equals(PositionInDB.RYADOVOI.getPosition())) {
            employee.setPositionId(PositionInDB.RYADOVOI.getId());
        } else if (valuesInFields[8].equals(PositionInDB.MANAGER.getPosition())) {
            employee.setPositionId(PositionInDB.MANAGER.getId());
        } else {
            employee.setPositionId(PositionInDB.ADMIN.getId());
        }
        try {
            employeeService.save(employee);
            JOptionPane.showMessageDialog(this, "Вы зарегистрированы!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Что то не так!");
        }
        backToStartFrame();
    }

    private JTextField createTextField(int y, int width) {
        JTextField textField = new JTextField();
        textField.setBounds(30, y, width, 30);
        return textField;
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        return label;
    }

    private void enterAuth(JTextField loginTextField,
                           JTextField passTextField,
                           JLabel errorLabel) {
        currentUser = employeeService.selectEmployeeByLogin(loginTextField.getText());
        if (Objects.nonNull(currentUser) && BCrypt.checkpw(passTextField.getText(), currentUser.getPassword())) {
            createMainMenu();
        } else {
            errorLabel.setVisible(true);
        }
        mainPanel.repaint();
    }

    private void createMainMenu() {
        mainPanel.removeAll();

        mainPanel.add(createLabel("Вы зашли под пользователем: ", 30, 30, 230, 30, COMMON_FONT));
        mainPanel.add(createLabel(currentUser.getLogin(), 235, 30, 300, 30, BOLD_FONT));

        JButton showContractsButt = new JButton("Информация о контрактах");
        showContractsButt.setBounds(30, 100, 250, 50);
        showContractsButt.addActionListener(e -> showContractsTable());
        showContractsButt.setForeground(Color.BLACK);
        showContractsButt.setBackground(AQUA);
        showContractsButt.setFocusable(false);
        mainPanel.add(showContractsButt);

        JButton showCustomersButt = new JButton("Информация о клиентах");
        showCustomersButt.setBounds(320, 100, 250, 50);
        showCustomersButt.addActionListener(e -> showCustomerTable());
        showCustomersButt.setForeground(Color.BLACK);
        showCustomersButt.setBackground(AQUA);
        showCustomersButt.setFocusable(false);
        mainPanel.add(showCustomersButt);

        JButton showEmployeesButt = new JButton("Информация о сотрудниках");
        showEmployeesButt.setBounds(30, 200, 250, 50);
        showEmployeesButt.addActionListener(e -> showEmployeesTable());
        showEmployeesButt.setForeground(Color.BLACK);
        showEmployeesButt.setBackground(AQUA);
        showEmployeesButt.setFocusable(false);
        mainPanel.add(showEmployeesButt);

        JButton showEquipmentsButt = new JButton("Информация об оборудовании");
        showEquipmentsButt.setBounds(320, 200, 250, 50);
        showEquipmentsButt.addActionListener(e -> showEquipmentsTable());
        showEquipmentsButt.setForeground(Color.BLACK);
        showEquipmentsButt.setBackground(AQUA);
        showEquipmentsButt.setFocusable(false);
        mainPanel.add(showEquipmentsButt);

        JButton showMissionsButt = new JButton("Информация о заданиях");
        showMissionsButt.setBounds(30, 300, 250, 50);
        showMissionsButt.addActionListener(e -> showMissionsTable());
        showMissionsButt.setForeground(Color.BLACK);
        showMissionsButt.setBackground(AQUA);
        showMissionsButt.setFocusable(false);
        mainPanel.add(showMissionsButt);

        JButton showPositionsButt = new JButton("Информация о ролях в бд");
        showPositionsButt.setBounds(320, 300, 250, 50);
        showPositionsButt.addActionListener(e -> showPositionsTable());
        showPositionsButt.setForeground(Color.BLACK);
        showPositionsButt.setBackground(AQUA);
        showPositionsButt.setFocusable(false);
        mainPanel.add(showPositionsButt);

        JButton showPrioriesButt = new JButton("Информация о приоритетах заданий");
        showPrioriesButt.setBounds(30, 400, 250, 50);
        showPrioriesButt.addActionListener(e -> showPrioritiesTable());
        showPrioriesButt.setForeground(Color.BLACK);
        showPrioriesButt.setBackground(AQUA);
        showPrioriesButt.setFocusable(false);
        mainPanel.add(showPrioriesButt);

        JButton createMissionButt = new JButton("Создать задание");
        createMissionButt.setBounds(320, 400, 250, 50);
        createMissionButt.addActionListener(e -> showCreateMissionFrame());
        createMissionButt.setForeground(Color.BLACK);
        createMissionButt.setBackground(GREEN);
        createMissionButt.setFocusable(false);
        mainPanel.add(createMissionButt);

        JButton createReportButt = new JButton("Создать отчет о сотруднике");
        createReportButt.setBounds(30, 500, 250, 50);
        createReportButt.addActionListener(e -> showCreateReportFrame());
        createReportButt.setForeground(Color.BLACK);
        createReportButt.setBackground(GREEN);
        createReportButt.setFocusable(false);
        mainPanel.add(createReportButt);

        JButton backButt = new JButton("Выйти из аккаунта");
        backButt.setBounds(320, 500, 250, 50);
        backButt.addActionListener(e -> backToStartFrame());
        backButt.setForeground(Color.BLACK);
        backButt.setBackground(SOFT_PINK);
        backButt.setFocusable(false);
        mainPanel.add(backButt);

        mainPanel.repaint();
    }

    private void showCreateReportFrame() {
        mainPanel.removeAll();

        mainPanel.add(createLabel("Создание отчета", 250, 30, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Выберете сотрудника:", 30, 65, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Дата начала периода (В формате yyyy-MM-dd):", 30, 130, 400, 30, COMMON_FONT));
        mainPanel.add(createLabel("Дата конца периода (В формате yyyy-MM-dd):", 30, 185, 400, 30, COMMON_FONT));

        List<Object> panelFields = new LinkedList<>();

        List<Employee> employeeList = employeeService.selectAllEmployees();
        String[] employeeArr = new String[employeeList.size()];
        Integer[] serviceNumEmplArr = new Integer[employeeList.size()];
        for (int i = 0; i < employeeList.size(); i++) {
            Employee employee = employeeList.get(i);
            String s = employee.getServiceNumber() + ", " + employee.getFirstName()
                    + ", " + employee.getLastName() + ", " + employee.getMiddleName()
                    + ", " + employee.getPhone() + ", " + employee.getEmail()
                    + ", " + employee.getFax() + ", " + employee.getLogin()
                    + ", " + employee.getPositionId();
            employeeArr[i] = s;
            serviceNumEmplArr[i] = employee.getServiceNumber();
        }
        DefaultComboBoxModel<String> cbModelForEmployee = new DefaultComboBoxModel<>(employeeArr);
        JComboBox<String> comboBoxForEmployee = new JComboBox<>(cbModelForEmployee);

        comboBoxForEmployee.setBounds(30, 90, 540, 30);
        panelFields.add(comboBoxForEmployee);
        mainPanel.add(comboBoxForEmployee);
        panelFields.add(createTextField(155, 200));
        mainPanel.add((JTextField) panelFields.get(1));
        panelFields.add(createTextField(210, 200));
        mainPanel.add((JTextField) panelFields.get(2));

        JButton submitButt = getStandardSubmitButton("Создать отчет");
        submitButt.addActionListener(e -> createReport(panelFields, serviceNumEmplArr));
        mainPanel.add(submitButt);

        JButton backButt = getStandardBackButton();
        mainPanel.add(backButt);

        mainPanel.repaint();
    }

    private JButton getStandardSubmitButton(String text) {
        JButton submitButton = new JButton(text);
        submitButton.setBounds(315, 480, 170, 50);
        submitButton.setForeground(Color.BLACK);
        submitButton.setBackground(AQUA);
        submitButton.setFocusable(false);
        return submitButton;
    }

    private JButton getStandardBackButton() {
        JButton backButton = new JButton("Назад");
        backButton.setBounds(500, 480, 70, 50);
        backButton.addActionListener(e -> createMainMenu());
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(SOFT_PINK);
        backButton.setFocusable(false);
        return backButton;
    }

    private void createReport(List<Object> fields, Integer[] serviceNums) {
        int exec = serviceNums[((JComboBox) fields.get(0)).getSelectedIndex()];
        Employee employeeReport = employeeService.selectEmployeeByServiceNumber(exec);
        String reportAbout = employeeReport.getFirstName() + " " + employeeReport.getMiddleName()
                + " " + employeeReport.getLastName();
        List<Mission> missionsByExecutor = missionService.selectMissionByExecutor(exec);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBegin;
        Date dateEnd;
        try {
            dateBegin = dateFormat.parse(((JTextField) fields.get(1)).getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Некорректная дата начала!");
            return;
        }
        try {
            dateEnd = dateFormat.parse(((JTextField) fields.get(2)).getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Некорректная дата конца!");
            return;
        }
        String dateBeginInString = dateFormat.format(dateBegin);
        String dateEndInString = dateFormat.format(dateEnd);
        int countCompleteInTime = 0;
        int countCompleteNotInTime = 0;
        int countNotCompleteNotInTime = 0;
        int countCurrentMissions = 0;
        List<Mission> missionsByExecutorInIntervalDates = new LinkedList<>();
        for (Mission mission : missionsByExecutor) {
            if (!(mission.getBeginDate().before(dateBegin) && mission.getBeginDate().after(dateEnd))) {
                missionsByExecutorInIntervalDates.add(mission);
            }
        }
        for (Mission mission : missionsByExecutorInIntervalDates) {
            if (Objects.nonNull(mission.getEndDate())) {
                if (Objects.nonNull(mission.getComplete())) {
                    if (mission.getEndDate().after(mission.getComplete())) {
                        countCompleteInTime++;
                    } else {
                        countCompleteNotInTime++;
                    }
                } else if (mission.getEndDate().after(new Date())) {
                    countCurrentMissions++;
                } else {
                    countNotCompleteNotInTime++;
                }
            } else {
                countCurrentMissions++;
            }
        }
        int countMissions = missionsByExecutorInIntervalDates.size();
        mainPanel.removeAll();

        mainPanel.add(createLabel("Отчет по сотруднику: ", 30, 30, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(reportAbout, 200, 30, 500, 60, BOLD_FONT));
        mainPanel.add(createLabel("Общее количество заданий с " + dateBeginInString + " до " + dateEndInString + ":", 30, 65, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(String.valueOf(countMissions), 410, 65, 500, 60, BOLD_FONT));
        mainPanel.add(createLabel("Количество заданий завершенных вовремя:", 30, 130, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(String.valueOf(countCompleteInTime), 325, 130, 500, 60, BOLD_FONT));
        mainPanel.add(createLabel("Количество заданий завершенных с нарушением срока выполнения:", 30, 195, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(String.valueOf(countCompleteNotInTime), 485, 195, 500, 60, BOLD_FONT));
        mainPanel.add(createLabel("Количество заданий с истекшим сроком исполнения не было завершено:", 30, 260, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(String.valueOf(countNotCompleteNotInTime), 515, 260, 500, 60, BOLD_FONT));
        mainPanel.add(createLabel("Количество текущих заданий:", 30, 325, 500, 60, COMMON_FONT));
        mainPanel.add(createLabel(String.valueOf(countCurrentMissions), 235, 325, 500, 60, BOLD_FONT));

        JButton backButt = getStandardBackButton();
        mainPanel.add(backButt);

        mainPanel.repaint();
    }
    //TODO: Продолжить рефакторинг отсюда.
    private void showCreateMissionFrame() {
        mainPanel.removeAll();

        mainPanel.add(createLabel("Создание задания", 250, 30, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Название (Обязательно):", 30, 65, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Описание (Обязательно):", 30, 115, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Дата начала (Обязательно) (В формате yyyy-MM-dd):", 30, 165, 350, 30, COMMON_FONT));
        mainPanel.add(createLabel("Дата конца (В формате yyyy-MM-dd):", 30, 215, 270, 30, COMMON_FONT));
        if (currentUser.getPositionId() != 2) {
            mainPanel.add(createLabel("Исполнитель:", 30, 265, 200, 30, COMMON_FONT));
        }
        mainPanel.add(createLabel("Приоритет:", 30, 315, 200, 30, COMMON_FONT));
        mainPanel.add(createLabel("Заказчик:", 30, 365, 200, 30, COMMON_FONT));

        List<Object> listTextFields = new LinkedList<>();

        DefaultComboBoxModel<String> cbModelForPriority = new DefaultComboBoxModel<>(new String[]{null, "Высокий", "Средний", "Низкий"});
        JComboBox<String> comboBoxForPriority = new JComboBox<>(cbModelForPriority);

        List<Employee> employeeList = employeeService.selectAllEmployees();
        if (currentUser.getPositionId() == 1) {
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getPositionId() != 2 && !employeeList.get(i).getServiceNumber().equals(currentUser.getServiceNumber())) {
                    employeeList.remove(i);
                    i--;
                }
            }
        }
        String[] employeeArr = new String[employeeList.size() + 1];
        Integer[] serviceNumEmployeeArr = new Integer[employeeArr.length];
        employeeArr[0] = null;
        serviceNumEmployeeArr[0] = null;
        for (int i = 0; i < employeeList.size(); i++) {
            Employee employee = employeeList.get(i);
            String s = employee.getServiceNumber() + ", " + employee.getFirstName()
                    + ", " + employee.getLastName() + ", " + employee.getMiddleName()
                    + ", " + employee.getPhone() + ", " + employee.getEmail()
                    + ", " + employee.getFax() + ", " + employee.getLogin()
                    + ", " + employee.getPositionId();
            employeeArr[i + 1] = s;
            serviceNumEmployeeArr[i + 1] = employee.getServiceNumber();
        }
        DefaultComboBoxModel<String> cbModelForEmployee = new DefaultComboBoxModel<>(employeeArr);
        JComboBox<String> comboBoxForEmployee = new JComboBox<>(cbModelForEmployee);

        List<Customer> customerList = customerService.selectAllCustomers();
        String[] customerArr = new String[customerList.size() + 1];
        Integer[] serviceNumCustomerArr = new Integer[customerArr.length];
        customerArr[0] = null;
        serviceNumCustomerArr[0] = null;
        for (int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            String s = customer.getCustomerId() + ", " + customer.getFirstName()
                    + ", " + customer.getLastName() + ", " + customer.getMiddleName()
                    + ", " + customer.getPhone() + ", " + customer.getEmail()
                    + ", " + customer.getInn();
            customerArr[i + 1] = s;
            serviceNumCustomerArr[i + 1] = customer.getCustomerId();
        }
        DefaultComboBoxModel<String> cbModelForCustomer = new DefaultComboBoxModel<>(customerArr);
        JComboBox<String> comboBoxForCustomer = new JComboBox<>(cbModelForCustomer);

        for (int i = 0; i < 7; i++) {
            if (i == 5) {
                comboBoxForPriority.setBounds(30, 90 + (i * 50), 200, 30);
                listTextFields.add(comboBoxForPriority);
                mainPanel.add(comboBoxForPriority);
            } else if (i == 4) {
                comboBoxForEmployee.setBounds(30, 90 + (i * 50), 500, 30);
                if (currentUser.getPositionId() == 2) {
                    comboBoxForEmployee.setVisible(false);
                }
                listTextFields.add(comboBoxForEmployee);
                mainPanel.add(comboBoxForEmployee);
            } else if (i == 6) {
                comboBoxForCustomer.setBounds(30, 90 + (i * 50), 500, 30);
                listTextFields.add(comboBoxForCustomer);
                mainPanel.add(comboBoxForCustomer);
            } else {
                listTextFields.add(createTextField(90 + (i * 50), 200));
                mainPanel.add((JTextField) listTextFields.get(i));
            }
        }

        JButton submitButt = getStandardSubmitButton("Создать");
        submitButt.addActionListener(e -> createNewMission(listTextFields, serviceNumEmployeeArr, serviceNumCustomerArr));
        mainPanel.add(submitButt);

        JButton backButt = getStandardBackButton();
        mainPanel.add(backButt);

        mainPanel.repaint();
    }

    private void createNewMission(List<Object> jTextFieldList, Integer[] employeeArr, Integer[] customerArr) {
        boolean success = true;
        Mission mission = new Mission();
        mission.setName(((JTextField) jTextFieldList.get(0)).getText());
        mission.setDescription(((JTextField) jTextFieldList.get(1)).getText());
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate = null;
        try {
            beginDate = myFormat.parse(((JTextField) jTextFieldList.get(2)).getText());
        } catch (ParseException e) {
            System.out.println("lol1");
            JOptionPane.showMessageDialog(this, "Неверная дата начала!");
            createMainMenu();
            return;
        }
        try {
            endDate = myFormat.parse(((JTextField) jTextFieldList.get(3)).getText());
        } catch (ParseException e) {
            System.out.println("lol2");
        }
        try {
            mission.setBeginDate(beginDate);
        } catch (NullPointerException e) {
            System.out.println("kek1");
            JOptionPane.showMessageDialog(this, "Неверная дата начала!");
            createMainMenu();
            return;
        }
        try {
            mission.setEndDate(endDate);
        } catch (NullPointerException e) {
            System.out.println("kek2");
        }
        mission.setAuthor(currentUser.getServiceNumber());

        try {
            if (currentUser.getPositionId() != 2) {
                int id = ((JComboBox) jTextFieldList.get(4)).getSelectedIndex();
                if (id != -1) {
                    mission.setExecutor(employeeArr[id]);
                }
            } else {
                mission.setExecutor(currentUser.getServiceNumber());
            }
        } catch (NullPointerException ignored) {
        }

        try {
            String priorityString = ((JComboBox) jTextFieldList.get(5)).getSelectedItem().toString();
            if (priorityString.equals("Низкий")) {
                mission.setPriorityId('3');
            } else if (priorityString.equals("Средний")) {
                mission.setPriorityId('2');
            } else if (priorityString.equals("Высокий")) {
                mission.setPriorityId('1');
            }
        } catch (NullPointerException ignored) {
        }

        try {
            int id = ((JComboBox) jTextFieldList.get(6)).getSelectedIndex();
            if (id != -1) {
                mission.setCustomerId(customerArr[id]);
            }
        } catch (NullPointerException ignored) {
        }
        try {
            missionService.save(mission);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Что то не так!");
            createMainMenu();
            success = false;
        }
        if (success) {
            JOptionPane.showMessageDialog(this, "Успешно!");
            createMainMenu();
        }
    }

    private void showPrioritiesTable() {
        JFrame prioritiesFrame = new JFrame();
        prioritiesFrame.setTitle("Priorities");
        prioritiesFrame.setBounds(400, 400, 600, 500);
        prioritiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel prioritiesPanel = new Panel();
        prioritiesFrame.add(prioritiesPanel);
        prioritiesFrame.setContentPane(prioritiesPanel);
        prioritiesFrame.setVisible(true);
        List<Priority> list = priorityService.selectAllPriorities();
        Object[][] array = new String[list.size()][2];
        Object[] columnsHeader = new String[]{"PriorityId", "Name"};
        JTable table = new JTable(array, columnsHeader);
        table.setEnabled(false);
        for (int i = 0; i < list.size(); i++) {
            Priority tmp = list.get(i);
            array[i][0] = tmp.getPriorityId().toString();
            array[i][1] = tmp.getName();
        }
        setTableColumnProperties(prioritiesFrame, prioritiesPanel, table);
    }

    private void setTableColumnProperties(JFrame prioritiesFrame, Panel prioritiesPanel, JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(160);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(580, 480));
        contents.add(new JScrollPane(table));
        prioritiesPanel.add(contents);
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.add(prioritiesFrame);
    }

    private void showPositionsTable() {
        JFrame positionsFrame = new JFrame();
        positionsFrame.setTitle("Positions");
        positionsFrame.setBounds(400, 400, 600, 500);
        positionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel positionsPanel = new Panel();
        positionsFrame.add(positionsPanel);
        positionsFrame.setContentPane(positionsPanel);
        positionsFrame.setVisible(true);
        List<Position> list = positionService.selectAllPositions();
        Object[][] array = new String[list.size()][2];
        Object[] columnsHeader = new String[]{"PositionId", "Tittle"};
        JTable table = new JTable(array, columnsHeader);
        table.setEnabled(false);
        for (int i = 0; i < list.size(); i++) {
            Position tmp = list.get(i);
            array[i][0] = tmp.getPositionId().toString();
            array[i][1] = tmp.getTitle();
        }
        setTableColumnProperties(positionsFrame, positionsPanel, table);
    }

    private void showMissionsTable() {
        JFrame missionsFrame = new JFrame();
        missionsFrame.setTitle("Missions");
        missionsFrame.setBounds(400, 400, 1200, 520);
        missionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel missionsPanel = new Panel();
        missionsFrame.setVisible(true);

        List<Mission> allMissions = missionService.selectAllMissions();
        List<Mission> missionToSee = new LinkedList<>();
        List<Mission> missionToChange = new LinkedList<>();
        allMissions.sort(Comparator.comparing(Mission::getMissionId));
        Object[][] array = new String[allMissions.size()][10];
        Object[] columnsHeader = new String[]{"MissionId", "Name",
                "Description", "BeginDate", "EndDate", "Complete", "Author", "Executor",
                "PriorityId", "CustomerId"};
        List<String[]> list1 = new ArrayList<>();

        List<Boolean[]> listBool = new ArrayList<>();
        Boolean[][] isEdit = new Boolean[allMissions.size()][10];

        for (int i = 0; i < allMissions.size(); i++) {
            Mission tmp = allMissions.get(i);
            if (currentUser.getPositionId() == 1) {
                Employee employee2 = employeeService.selectEmployeeByServiceNumber(tmp.getAuthor());
                if (!tmp.getAuthor().equals(currentUser.getServiceNumber())
                        && !Objects.equals(tmp.getExecutor(), currentUser.getServiceNumber())
                        && employee2.getPositionId() != 2) {
                    continue;
                }
            }
            if (currentUser.getPositionId() == 2) {
                if (!tmp.getAuthor().equals(currentUser.getServiceNumber())
                        && !Objects.equals(tmp.getExecutor(), currentUser.getServiceNumber())) {
                    continue;
                }
            }
            missionToSee.add(tmp);
            if (currentUser.getPositionId() == 3) {
                //all true
                for (int j = 0; j < 10; j++) {
                    isEdit[i][j] = true;
                }
            } else if (Objects.isNull(tmp.getComplete())) {
                if (currentUser.getServiceNumber().equals(tmp.getAuthor()) && currentUser.getPositionId() == 1) {
                    //all except author
                    for (int j = 0; j < 10; j++) {
                        isEdit[i][j] = j != 6;
                    }
                } else if (currentUser.getServiceNumber().equals(tmp.getAuthor()) && currentUser.getPositionId() == 2) {
                    //all except author and executor
                    for (int j = 0; j < 10; j++) {
                        isEdit[i][j] = j != 6 && j != 7;
                    }
                } else {
                    // only dateEnd and Complete
                    for (int j = 0; j < 10; j++) {
                        isEdit[i][j] = j == 4 || j == 5;
                    }
                }
            } else {
                // all false
                for (int j = 0; j < 10; j++) {
                    isEdit[i][j] = false;
                }
            }
            listBool.add(isEdit[i]);
            array[i][0] = tmp.getMissionId().toString();
            array[i][1] = tmp.getName();
            array[i][2] = tmp.getDescription();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            array[i][3] = formatter1.format(tmp.getBeginDate());
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                array[i][4] = formatter.format(tmp.getEndDate());
            } catch (NullPointerException ignored) {
            }
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                array[i][5] = formatter.format(tmp.getComplete());
            } catch (NullPointerException ignored) {
            }
            try {
                array[i][6] = tmp.getAuthor().toString();
            } catch (NullPointerException ignored) {
            }
            try {
                array[i][7] = tmp.getExecutor().toString();
            } catch (NullPointerException ignored) {
            }
            try {
                array[i][8] = tmp.getPriorityId().toString();
            } catch (NullPointerException ignored) {
            }
            try {
                array[i][9] = tmp.getCustomerId().toString();
            } catch (NullPointerException ignored) {
            }
            list1.add((String[]) array[i]);
        }
        Object[][] array2 = new String[list1.size()][10];
        for (int i = 0; i < list1.size(); i++) {
            array2[i] = list1.get(i);
        }

        Boolean[][] arrayBool2 = new Boolean[listBool.size()][10];
        for (int i = 0; i < listBool.size(); i++) {
            arrayBool2[i] = listBool.get(i);
        }
        MyTable table = new MyTable(array2, columnsHeader);
        table.setIsEdit(arrayBool2);
        JButton findButt = new JButton("Принять изменения");
        findButt.setVisible(false);
        table.getModel().addTableModelListener(e -> {
            findButt.setVisible(true);
            Mission tmp = missionToSee.get(e.getFirstRow());
            int column = e.getColumn();
            if (column == 0) {
                tmp.setMissionId(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            } else if (column == 1) {
                tmp.setName(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 2) {
                tmp.setDescription(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 3) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    tmp.setBeginDate(formatter1.parse(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            } else if (column == 4) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    tmp.setEndDate(formatter1.parse(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            } else if (column == 5) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    tmp.setComplete(formatter1.parse(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            } else if (column == 6) {
                tmp.setAuthor(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            } else if (column == 7) {
                tmp.setExecutor(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            } else if (column == 8) {
                tmp.setPriorityId(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString().charAt(0));
            } else if (column == 9) {
                tmp.setCustomerId(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            }
            missionToChange.add(tmp);
        });
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(5).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(6).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(7).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(8).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(9).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(140);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(140);
        columnModel.getColumn(5).setPreferredWidth(180);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(100);
        columnModel.getColumn(8).setPreferredWidth(100);
        columnModel.getColumn(9).setPreferredWidth(100);

        findButt.setBounds(150, 30, 80, 30);
        findButt.setForeground(Color.BLACK);
        findButt.setBackground(AQUA);
        findButt.setFocusable(false);
        findButt.addActionListener(e -> updateMissions(missionToChange, findButt));
        missionsPanel.add(findButt);

        setBoxContents(missionsFrame, missionsPanel, table);
    }

    private void updateMissions(List<Mission> missionToChange, JButton findButt) {
        findButt.setVisible(false);
        for (Mission mission : missionToChange) {
            System.out.println(mission);
            try {
                missionService.update(mission);
                missionToChange.remove(mission);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setBoxContents(JFrame missionsFrame, Panel missionsPanel, JTable table) {
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(1180, 480));
        contents.add(new JScrollPane(table));
        missionsFrame.add(missionsPanel);
        missionsFrame.setContentPane(missionsPanel);
        missionsPanel.add(contents);
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.add(missionsFrame);
    }

    private void showEquipmentsTable() {
        JFrame equipmentsFrame = new JFrame();
        equipmentsFrame.setTitle("Equipments");
        equipmentsFrame.setBounds(400, 400, 800, 500);
        equipmentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel equipmentsPanel = new Panel();
        equipmentsFrame.add(equipmentsPanel);
        equipmentsFrame.setContentPane(equipmentsPanel);
        equipmentsFrame.setVisible(true);
        List<Equipment> list = equipmentService.selectAllEquipments();
        Object[][] array = new String[list.size()][5];
        Object[] columnsHeader = new String[]{"SerNumber", "Type",
                "Name", "Options", "Number"};
        JTable table = new JTable(array, columnsHeader);
        table.setEnabled(false);
        for (int i = 0; i < list.size(); i++) {
            Equipment tmp = list.get(i);
            array[i][0] = tmp.getSerNumber();
            array[i][1] = tmp.getType();
            array[i][2] = tmp.getName();
            array[i][3] = tmp.getOptions();
            try {
                array[i][4] = tmp.getNumber().toString();
            } catch (NullPointerException ignored) {
            }
        }
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(300);
        columnModel.getColumn(4).setPreferredWidth(70);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(780, 480));
        contents.add(new JScrollPane(table));
        equipmentsPanel.add(contents);
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.add(equipmentsFrame);
    }

    private void showEmployeesTable() {
        JFrame employeesFrame = new JFrame();
        employeesFrame.setTitle("Employees");
        employeesFrame.setBounds(400, 400, 1200, 520);
        employeesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel employeesPanel = new Panel();
        employeesFrame.setVisible(true);
        List<Employee> list = employeeService.selectAllEmployees();

        List<Employee> employeeToChange = new LinkedList<>();
        Object[][] array;
        Object[] columnsHeader;
        if (currentUser.getPositionId() != 3) {
            array = new String[list.size()][9];
            columnsHeader = new String[]{"ServiceNumber", "FirstName",
                    "LastName", "MiddleName", "Phone", "Email", "FAX", "Login",
                    "PositionId"};
        } else {
            array = new String[list.size()][10];
            columnsHeader = new String[]{"ServiceNumber", "FirstName",
                    "LastName", "MiddleName", "Phone", "Email", "FAX", "Login",
                    "Password", "PositionId"};
        }
        JTable table = new JTable(array, columnsHeader);
        if (currentUser.getPositionId() != 3) {
            table.setEnabled(false);
        }
        for (int i = 0; i < list.size(); i++) {
            Employee tmp = list.get(i);
            array[i][0] = tmp.getServiceNumber().toString();
            array[i][1] = tmp.getFirstName();
            array[i][2] = tmp.getLastName();
            array[i][3] = tmp.getMiddleName();
            array[i][4] = tmp.getPhone();
            array[i][5] = tmp.getEmail();
            array[i][6] = tmp.getFax();
            array[i][7] = tmp.getLogin();
            if (currentUser.getPositionId() == 3) {
                array[i][8] = tmp.getPassword();
                try {
                    array[i][9] = tmp.getPositionId().toString();
                } catch (NullPointerException ignored) {
                }
            } else {
                try {
                    array[i][8] = tmp.getPositionId().toString();
                } catch (NullPointerException ignored) {
                }
            }
        }
        JButton findButt = new JButton("Принять изменения");
        findButt.setVisible(false);
        table.getModel().addTableModelListener(e -> {
            findButt.setVisible(true);
            Employee tmp = list.get(e.getFirstRow());
            int column = e.getColumn();
            if (column == 0) {
                tmp.setServiceNumber(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            } else if (column == 1) {
                tmp.setFirstName(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 2) {
                tmp.setLastName(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 3) {
                tmp.setMiddleName(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 4) {
                tmp.setPhone(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 5) {
                tmp.setEmail(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 6) {
                tmp.setFax(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 7) {
                tmp.setLogin(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString());
            } else if (column == 8) {
                tmp.setPassword(BCrypt.hashpw(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString(), BCrypt.gensalt()));
            } else if (column == 9) {
                tmp.setPositionId(Integer.parseInt(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString()));
            }
            employeeToChange.add(tmp);
        });

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(5).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(6).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(7).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(8).setCellRenderer(new WordWrapCellRenderer());
        if (currentUser.getPositionId() == 3) {
            columnModel.getColumn(9).setCellRenderer(new WordWrapCellRenderer());
        }
        columnModel.getColumn(0).setPreferredWidth(130);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(160);
        columnModel.getColumn(3).setPreferredWidth(160);
        columnModel.getColumn(4).setPreferredWidth(140);
        columnModel.getColumn(5).setPreferredWidth(245);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(180);
        columnModel.getColumn(8).setPreferredWidth(300);
        if (currentUser.getPositionId() == 3) {
            columnModel.getColumn(9).setPreferredWidth(120);
        }
        findButt.setBounds(150, 30, 80, 30);
        findButt.setForeground(Color.BLACK);
        findButt.setBackground(AQUA);
        findButt.setFocusable(false);
        findButt.addActionListener(e -> updateEmployers(employeeToChange, findButt));
        employeesPanel.add(findButt);

        setBoxContents(employeesFrame, employeesPanel, table);
    }

    private void updateEmployers(List<Employee> employeeToChange, JButton findButt) {
        findButt.setVisible(false);
        for (Employee employee : employeeToChange) {
            System.out.println(employee);
            try {
                employeeService.update(employee);
                employeeToChange.remove(employee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showCustomerTable() {
        JFrame customerFrame = new JFrame();
        customerFrame.setTitle("Customers");
        customerFrame.setBounds(400, 400, 820, 620);
        customerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel customerPanel = new Panel();
        customerFrame.add(customerPanel);
        customerFrame.setContentPane(customerPanel);
        customerFrame.setVisible(true);

        JTextField findField = createTextField(30, 100);
        findField.setPreferredSize(new Dimension(100, 30));
        customerPanel.add(findField);
        JButton findButt = new JButton("Поиск");
        findButt.setBounds(150, 30, 80, 30);
        findButt.setForeground(Color.BLACK);
        findButt.setBackground(AQUA);
        findButt.setFocusable(false);

        JButton findNotButt = new JButton("Отменить поиск");
        findNotButt.setBounds(240, 30, 100, 30);
        findNotButt.setForeground(Color.BLACK);
        findNotButt.setBackground(SOFT_PINK);
        findNotButt.setFocusable(false);
        findNotButt.setVisible(false);

        List<Customer> list = customerService.selectAllCustomers();
        Object[][] array = new String[list.size()][7];
        Object[] columnsHeader = new String[]{"CustomerId", "FirstName",
                "LastName", "MiddleName", "Phone", "Email", "INN"};
        JTable table = new JTable(array, columnsHeader);
        table.setEnabled(false);
        for (int i = 0; i < list.size(); i++) {
            Customer tmp = list.get(i);
            array[i][0] = tmp.getCustomerId().toString();
            array[i][1] = tmp.getFirstName();
            array[i][2] = tmp.getLastName();
            array[i][3] = tmp.getMiddleName();
            array[i][4] = tmp.getPhone();
            array[i][5] = tmp.getEmail();
            array[i][6] = tmp.getInn();
        }
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(5).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(6).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(160);
        columnModel.getColumn(3).setPreferredWidth(160);
        columnModel.getColumn(4).setPreferredWidth(140);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(120);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(800, 480));
        contents.add(new JScrollPane(table));
        findButt.addActionListener(e -> {
            findCustomer(findField, array);
            if (!findField.getText().equals("")) {
                findNotButt.setVisible(true);
            }
        });
        customerPanel.add(findButt);
        findNotButt.addActionListener(e -> {
            findCustomer(new JTextField(""), array);
            findField.setText("");
            findNotButt.setVisible(false);
        });
        customerPanel.add(findNotButt, 2);
        customerPanel.add(contents, 3);
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.add(customerFrame);
    }

    private void findCustomer(JTextField field, Object[][] array) {
        List<String[]> list = new LinkedList<>();
        for (Object[] objects : array) {
            if (Arrays.toString(objects).toLowerCase().contains(field.getText().toLowerCase())) {
                list.add((String[]) objects);
            }
        }
        Object[][] array2 = new String[list.size()][7];
        for (int i = 0; i < list.size(); i++) {
            array2[i] = list.get(i);
        }
        JFrame frame = null;
        for (JFrame activeFrame : activeFrames) {
            if (activeFrame.isVisible()) {
                frame = activeFrame;
            }
        }
        frame.getContentPane().remove(3);

        Object[] columnsHeader = new String[]{"CustomerId", "FirstName",
                "LastName", "MiddleName", "Phone", "Email", "INN"};
        JTable table = new JTable(array2, columnsHeader);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(5).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(6).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(160);
        columnModel.getColumn(3).setPreferredWidth(160);
        columnModel.getColumn(4).setPreferredWidth(140);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(120);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(800, 480));
        contents.add(new JScrollPane(table));
        frame.getContentPane().add(contents);
        frame.repaint();
        frame.revalidate();
    }

    private void showContractsTable() {
        JFrame contractFrame = new JFrame();
        contractFrame.setTitle("Contracts");
        contractFrame.setBounds(400, 400, 800, 500);
        contractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Panel contractPanel = new Panel();
        contractFrame.add(contractPanel);
        contractFrame.setContentPane(contractPanel);
        contractFrame.setVisible(true);
        List<Contract> list = contractService.selectAllContracts();
        Object[][] array = new String[list.size()][4];
        Object[] columnsHeader = new String[]{"Number", "DateConclusion",
                "Description", "CustomerId"};
        JTable table = new JTable(array, columnsHeader);
        table.setEnabled(false);
        for (int i = 0; i < list.size(); i++) {
            Contract tmp = list.get(i);
            array[i][0] = tmp.getNumber().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            array[i][1] = formatter.format(tmp.getDateConclusion());
            array[i][2] = tmp.getDescription();
            array[i][3] = tmp.getCustomerId().toString();

        }
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new WordWrapCellRenderer());
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(100);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setPreferredSize(new Dimension(780, 480));
        contents.add(new JScrollPane(table));
        contractPanel.add(contents);
        activeFrames.forEach(e -> e.setVisible(false));
        activeFrames.add(contractFrame);
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

    class MyTable extends JTable {
        Boolean[][] isEdit;

        public MyTable(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
        }

        public void setIsEdit(Boolean[][] isEdit) {
            this.isEdit = isEdit;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return isEdit[row][column];
        }
    }


    static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        public WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(false);
            setOpaque(true);
        }

        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(),
                    getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height + 30);
            }
            return this;
        }
    }
}
