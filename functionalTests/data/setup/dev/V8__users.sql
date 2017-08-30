INSERT INTO roles
(name, description) VALUES
('FacilityHead', ''),
('ReportViewer', ''),
('Supervisor', '');

INSERT INTO role_rights
(roleId, rightName) VALUES
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_PRODUCT'),
((SELECT id FROM roles WHERE name = 'FacilityHead'), 'AUTHORIZE_REQUISITION'),
((SELECT id FROM roles WHERE name = 'FacilityHead'), 'VIEW_REQUISITION'),
((SELECT id FROM roles WHERE name = 'FacilityHead'), 'CREATE_REQUISITION'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_REQUISITION_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_STOCK_ON_HAND_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_TABLET_INFO_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_STOCKOUT_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_EXPIRY_DATES_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_TRACER_DRUGS_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_CONSUMPTION_MOVEMENTS_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_ADJUSTMENT_OCCURRENCES_REPORT'),
((SELECT id FROM roles WHERE name = 'ReportViewer'), 'VIEW_LAST_SYNC_TIME_REPORTS'),
((SELECT id FROM roles WHERE name = 'Supervisor'), 'VIEW_REQUISITION'),
((SELECT id FROM roles WHERE name = 'Supervisor'), 'APPROVE_REQUISITION');

INSERT INTO users
(userName, password, facilityId, firstName, lastName, email, verified, active, restrictLogin, isMobileUser) VALUES
('central', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'CENTRAL'), 'Central', 'User', 'central@test.com',
  TRUE, TRUE, FALSE, FALSE),
('superuser', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'HF5'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('mmia_mismatch', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_MMIA_MISMATCH'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('mmia', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_MMIA'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('via', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_VIA'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('stock_card', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_STOCKCARD'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('kit', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
    (SELECT id FROM facilities WHERE code = 'F_KIT'), 'Super', 'User', NULL,
    TRUE, TRUE, FALSE, TRUE),
('physical_inventory', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_PHYSICAL_INVENTORY'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('initial_inventory', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_INITIAL_INVENTORY'), 'Super', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE),
('professor_x', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'DDM1'), 'Charles', 'Xavier', 'openlmis.test.ddm@gmail.com',
  TRUE, TRUE, FALSE, TRUE),
('magneto', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'DPS1'), 'Eric', 'Lensherr', 'openlmis.test.dpm@gmail.com',
  TRUE, TRUE, FALSE, TRUE),
('mystique', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'HF2'), 'Raven', 'Darkholme', NULL,
  TRUE, TRUE, FALSE, TRUE),
('wolverine', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'HF3'), 'Logan', 'H', NULL,
  TRUE, TRUE, FALSE, TRUE),
('core', 'vFR3ULknlislVs2ESzJvdXN330IYhUdA6FnraiiZWqJKmtJGELNqaLwC2iiQUHuUWcK6hPtZGkJmkRT8zXLI5212gieie',
  (SELECT id FROM facilities WHERE code = 'F_CORE'), 'Core', 'User', NULL,
  TRUE, TRUE, FALSE, TRUE);

INSERT INTO role_assignments
(userId, roleId, programId, supervisoryNodeId) VALUES
((SELECT ID FROM USERS WHERE username = 'superuser'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'superuser'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'superuser'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'superuser'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'superuser'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'mmia_mismatch'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia_mismatch'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia_mismatch'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia_mismatch'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia_mismatch'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'mmia'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'mmia'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'via'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'via'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'via'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'via'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'via'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'kit'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'kit'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'kit'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'kit'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'kit'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'physical_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'physical_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'physical_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'physical_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'physical_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'initial_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'initial_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'initial_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'initial_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'initial_inventory'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'mystique'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mystique'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'mystique'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'mystique'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'mystique'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'wolverine'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'wolverine'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'wolverine'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'wolverine'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'wolverine'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),

((SELECT ID FROM USERS WHERE username = 'core'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'MMIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'core'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'VIA'), NULL),
((SELECT ID FROM USERS WHERE username = 'core'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'ESS_MEDS'), NULL),
((SELECT ID FROM USERS WHERE username = 'core'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'TB'), NULL),
((SELECT ID FROM USERS WHERE username = 'core'),
  (SELECT id FROM roles WHERE name = 'FacilityHead'), (SELECT id FROM programs WHERE code = 'NUTRITION'), NULL),


((SELECT ID FROM USERS WHERE username = 'professor_x'),
  (SELECT id FROM roles WHERE name = 'ReportViewer'), NULL, NULL),

((SELECT ID FROM USERS WHERE username = 'magneto'),
  (SELECT id FROM roles WHERE name = 'ReportViewer'), NULL, NULL),

((SELECT ID FROM USERS WHERE username = 'professor_x'),
  (SELECT id FROM roles WHERE name = 'Supervisor'), (SELECT id FROM programs WHERE code = 'VIA'),
  (SELECT id FROM supervisory_nodes WHERE code = 'N2')),

((SELECT ID FROM USERS WHERE username = 'magneto'),
  (SELECT id FROM roles WHERE name = 'Supervisor'), (SELECT id FROM programs WHERE code = 'MMIA'),
  (SELECT id FROM supervisory_nodes WHERE code = 'N1')),

((SELECT ID FROM USERS WHERE username = 'central'),
  (SELECT id FROM roles WHERE name = 'ReportViewer'), NULL, NULL);
