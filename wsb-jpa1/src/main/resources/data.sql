INSERT INTO PATIENT (
    id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, active, version
) VALUES
      (101, 'Jan', 'Kowalski', '123456789', 'jan.kowalski@email.com', 'P123', '1980-05-15', true, 0),
      (102, 'Anna', 'Nowak', '987654321', 'anna.nowak@email.com', 'P124', '1992-08-25', true, 0),
      (103, 'Tomasz', 'Zieliński', '111111111', 'tomasz.zielinski@email.com', 'P125', '1975-03-20', false, 0),
      (104, 'Ewa', 'Kowalska', '222222222', 'ewa.kowalska@email.com', 'P126', '1990-12-01', true, 0),
      (105, 'Marek', 'Nowak', '333333333', 'marek.nowak@email.com', 'P127', '1985-07-11', true, 0),
      (106, 'Julia', 'Wiśniewska', '444444444', 'julia.wisniewska@email.com', 'P128', '1988-04-30', false, 0);

-- Dodanie doktorów
INSERT INTO DOCTOR (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
VALUES
    (201, 'Piotr', 'Lewandowski', '111222333', 'piotr.lewandowski@email.com', 'D456', 'OCULIST'),
    (202, 'Maria', 'Wiśniewska', '444555666', 'maria.wisniewska@email.com', 'D789', 'DERMATOLOGIST');

-- Dodanie adresów
INSERT INTO ADDRESS (id, address_line1, address_line2, city, postal_code, patient_id, doctor_id)
VALUES
    (901, 'ul. Klonowa 10', 'mieszkanie 5', 'Warszawa', '60-400', 101, NULL),
    (902, 'ul. Dębowa 15', 'dom', 'Kraków', '30-002', 101, NULL),
    (903, 'ul. Bukowa 8', 'blok 3A', 'Łódź', '90-003', 102, NULL),
    (904, 'ul. Modrzewiowa 30', 'blok 3A', 'Warszawa', '00-006', NULL, 201),
    (905, 'ul. Kasztanowa 25', 'gabinet prywatny', 'Poznań', '60-005', NULL, 202);

-- Dodanie wizyt
INSERT INTO VISIT (id, description, time, doctor_id, patient_id)
VALUES
    (301, 'Konsultacja kardiologiczna', '2024-04-10 10:30:00', 201, 101),
    (302, 'Badanie kontrolne', '2024-04-11 15:00:00', 202, 102),
    (303, 'Wizyta kontrolna', '2024-05-15 12:00:00', 201, 101),  -- Wizyta dla Jana Kowalskiego
    (304, 'Konsultacja dermatologiczna', '2024-05-17 14:00:00', 202, 102), -- Wizyta dla Anny Nowak
    (305, 'Konsultacja kardiologiczna', '2024-05-20 10:30:00', 201, 104), -- Wizyta dla Ewy Kowalskiej
    (306, 'Badanie kontrolne', '2024-05-22 16:00:00', 202, 105), -- Wizyta dla Marka Nowaka
    (307, 'Wizyta kontrolna', '2024-06-10 10:00:00', 202, 101);  -- Now Jan Kowalski has 3 visits

-- Dodanie zabiegów medycznych
INSERT INTO MEDICAL_TREATMENT (id, description, type, visit_id)
VALUES
    (401, 'Angioplastyka', 'EKG', 301),
    (402, 'Terapia laserowa', 'RTG', 302),
    (403, 'Mikrofalowe leczenie', 'RTG', 303),
    (404, 'Zabieg kardiologiczny', 'EKG', 305);
