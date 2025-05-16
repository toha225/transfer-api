-- процедура пересчета баланса
CREATE
OR REPLACE FUNCTION update_account_balance()
RETURNS void
LANGUAGE plpgsql
AS $$
BEGIN
UPDATE account
SET balance = LEAST(balance * 1.1, initial_deposit * 2.07);
END;
$$;