-- Sample admin user
INSERT INTO users (name, email, password, phone, address, role, balance, created_at)
VALUES ('Admin User', 'admin@telecom.com', 'admin123', '9999999999', 'Admin Office', 'ADMIN', 0.0, NOW())
ON DUPLICATE KEY UPDATE name=name;

-- Sample regular users
INSERT INTO users (name, email, password, phone, address, role, balance, created_at)
VALUES 
('John Doe', 'john@example.com', 'password123', '9876543210', '123 Main St', 'USER', 500.0, NOW()),
('Jane Smith', 'jane@example.com', 'password123', '9876543211', '456 Oak Ave', 'USER', 300.0, NOW()),
('Bob Johnson', 'bob@example.com', 'password123', '9876543212', '789 Pine Rd', 'USER', 750.0, NOW())
ON DUPLICATE KEY UPDATE name=name;

-- Sample recharge plans
INSERT INTO recharge_plans (name, description, price, validity_days, data, voice, sms, plan_type, is_active, created_at, updated_at)
VALUES 
('Basic Prepaid', 'Basic plan with essential services', 99.0, 28, '1GB', '300 mins', '100 SMS', 'PREPAID', true, NOW(), NOW()),
('Standard Prepaid', 'Standard plan with good data allowance', 199.0, 28, '3GB', 'Unlimited', '300 SMS', 'PREPAID', true, NOW(), NOW()),
('Premium Prepaid', 'Premium plan with high data and unlimited calls', 399.0, 28, '10GB', 'Unlimited', 'Unlimited', 'PREPAID', true, NOW(), NOW()),
('Data Only Small', 'Data only plan for light users', 149.0, 30, '2GB', 'No Voice', 'No SMS', 'DATA_ONLY', true, NOW(), NOW()),
('Data Only Large', 'Data only plan for heavy users', 499.0, 30, '25GB', 'No Voice', 'No SMS', 'DATA_ONLY', true, NOW(), NOW()),
('Postpaid Basic', 'Basic postpaid plan', 299.0, 30, '5GB', 'Unlimited', '500 SMS', 'POSTPAID', true, NOW(), NOW()),
('Postpaid Premium', 'Premium postpaid with unlimited everything', 799.0, 30, 'Unlimited', 'Unlimited', 'Unlimited', 'POSTPAID', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=name;