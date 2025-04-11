INSERT INTO PATIENT (
  id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, active
) VALUES (
  101, 'Jan', 'Kowalski', '123456789', 'jan.kowalski@email.com', 'P123', '1980-05-15', true
), (
  102, 'Anna', 'Nowak', '987654321', 'anna.nowak@email.com', 'P124', '1992-08-25', true
);


INSERT INTO DOCTOR (id, first_name, last_name, telephone_number, email, doctor_number, specialization)
VALUES
    (201, 'Piotr', 'Lewandowski', '111222333', 'piotr.lewandowski@email.com', 'D456', 'OCULIST'),
    (202, 'Maria', 'Wiśniewska', '444555666', 'maria.wisniewska@email.com', 'D789', 'DERMATOLOGIST');

INSERT INTO ADDRESS (id, address_line1, address_line2, city, postal_code, patient_id, doctor_id)
VALUES
    (901, 'ul. Klonowa 10', 'mieszkanie 5', 'Warszawa', '60-400', 101, NULL), -- zmieniony kod pocztowy
    (902, 'ul. Dębowa 15', 'dom', 'Kraków', '30-002', 101, NULL),
    (903, 'ul. Bukowa 8', 'blok 3A', 'Łódź', '90-003', 102, NULL),
    (904, 'ul. Modrzewiowa 30', 'blok 3A', 'Warszawa', '00-006', NULL, 201),
    (905, 'ul. Kasztanowa 25', 'gabinet prywatny', 'Poznań', '60-005', NULL, 202);


INSERT INTO VISIT (id, description, time, doctor_id, patient_id)
VALUES
    (301, 'Konsultacja kardiologiczna', '2024-04-10 10:30:00', 201, 101),
    (302, 'Badanie kontrolne', '2024-04-11 15:00:00', 202, 102);

INSERT INTO MEDICAL_TREATMENT (id, description, type, visit_id)
VALUES
    (401, 'Angioplastyka', 'EKG', 301),
    (402, 'Terapia laserowa', 'RTG', 302);


