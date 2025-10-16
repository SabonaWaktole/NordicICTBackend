-- V7_1__remove_duplicate_admin_emails.sql

-- Delete duplicate emails in admin table, keep the row with the smallest id
DELETE FROM admin_model
WHERE id NOT IN (
    SELECT MIN(id)
    FROM admin
    GROUP BY email
    HAVING COUNT(*) > 1
);

-- Optional: check duplicates (for debugging)
-- SELECT email, COUNT(*) FROM admin GROUP BY email HAVING COUNT(*) > 1;
