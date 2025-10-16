
DELETE FROM admin_model
WHERE id NOT IN (
    SELECT MIN(id)
    FROM admin
    GROUP BY email
    HAVING COUNT(*) > 1
);
