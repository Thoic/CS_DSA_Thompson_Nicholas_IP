DROP PROCEDURE IF EXISTS ip_query1
DROP PROCEDURE IF EXISTS ip_query2
DROP PROCEDURE IF EXISTS ip_query3
DROP PROCEDURE IF EXISTS ip_query4
DROP PROCEDURE IF EXISTS ip_query5
DROP PROCEDURE IF EXISTS ip_query5_2
DROP PROCEDURE IF EXISTS ip_query6
DROP PROCEDURE IF EXISTS ip_query7
DROP PROCEDURE IF EXISTS ip_query8
DROP PROCEDURE IF EXISTS ip_query9
DROP PROCEDURE IF EXISTS ip_query10
DROP PROCEDURE IF EXISTS ip_query11
DROP PROCEDURE IF EXISTS ip_query12
DROP PROCEDURE IF EXISTS ip_query13
DROP PROCEDURE IF EXISTS ip_query14
DROP PROCEDURE IF EXISTS ip_query15

--Enter a new employee
GO
CREATE PROCEDURE ip_query1
    @type INT,
    @name VARCHAR(64),
    @address VARCHAR(64),
    @salary DECIMAL(18,2),
    @product_type VARCHAR(64),
    @max_products_per_day INT,
    @technical_position VARCHAR(64),
    @degrees VARCHAR(64)
AS
BEGIN
    IF @type = 1
        INSERT INTO quality_controller VALUES (@name, @address, @salary, @product_type);
    ELSE IF @type = 2
        INSERT INTO worker VALUES (@name, @address, @salary, @max_products_per_day);
    ELSE IF @type = 3
    BEGIN
        INSERT INTO technical_staff VALUES(@name, @address, @salary, @technical_position);
        IF @degrees != ''
            INSERT INTO graduate_technical_staff (sname, degree) SELECT @name,value FROM STRING_SPLIT(@degrees, ',');
    END;
END

--Enter a new product associated with the person who made the product, repaired the product if it is repaired, or checked the product
GO
CREATE PROCEDURE ip_query2
    @type INT,
    @product_id INT,
    @size VARCHAR(8),
    @software_name VARCHAR(64),
    @color VARCHAR(64),
    @weight DECIMAL(18,2),
    @wname VARCHAR(64),
    @prod_date DATE,
    @prod_dur DECIMAL(18,2),
    @qname VARCHAR(64),
    @sname VARCHAR(64),
    @repair_date DATE,
    @complaint_id INT
AS
BEGIN
    --check if worker can check the selected product type
    DECLARE @msg VARCHAR(64);
    IF (SELECT product_type FROM quality_controller WHERE qname = @qname) != CONCAT('product', @type)
    BEGIN
        SET @msg = @qname + ' cannot check products of this type.';
        THROW 50000, @msg, 1;
    END;

    --check if size is small, medium, or large
    IF NOT (@size != 'small' OR @size != 'medium' OR @size != 'large')
    BEGIN
        SET @msg = @size + ' is not a valid size of product.';
        THROW 50000, @msg, 1;
    END;

    INSERT INTO product VALUES (@product_id, @size);
    IF @type = 1
        INSERT INTO product1 VALUES (@product_id, @software_name);
    ELSE IF @type = 2
        INSERT INTO product2 VALUES (@product_id, @color);
    ELSE
        INSERT INTO product3 VALUES (@product_id, @weight);
    INSERT INTO makes VALUES (@product_id, @wname, @prod_date, @prod_dur);
    INSERT INTO tests VALUES (@product_id, @qname);
    IF @sname != ''
    BEGIN
        INSERT INTO repairs VALUES (@product_id, @sname, @repair_date)

        IF @complaint_id >= 0
            INSERT INTO complaint_repair VALUES (@product_id, @complaint_id);
        ELSE
            INSERT INTO requests_repair VALUES (@product_id, @qname);
    END;
END;

--Enter new customer associated with some products
GO
CREATE PROCEDURE ip_query3
    @cname VARCHAR(64),
    @address VARCHAR(64),
    @product_ids VARCHAR(64)
AS
BEGIN
    INSERT INTO customer VALUES (@cname, @address);
    INSERT INTO purchases (cname, product_id) SELECT @cname,PARSE(value AS INT) FROM STRING_SPLIT(@product_ids, ',');
END

--Create a new account associated with a product
GO
CREATE PROCEDURE ip_query4
    @account_no INT,
    @date_established DATE,
    @cost DECIMAL(18,2),
    @product_id INT
AS
BEGIN
    IF @product_id IN (SELECT product_id FROM product1)
    BEGIN
        INSERT INTO product1_account VALUES (@account_no, @date_established, @cost)
        INSERT INTO records_cost1 VALUES (@product_id, @account_no)
    END;
    
    ELSE IF @product_id IN (SELECT product_id FROM product2)
    BEGIN
        INSERT INTO product2_account VALUES (@account_no, @date_established, @cost)
        INSERT INTO records_cost2 VALUES (@product_id, @account_no)
    END;

    ELSE IF @product_id IN (SELECT product_id FROM product3)
    BEGIN
        INSERT INTO product3_account VALUES (@account_no, @date_established, @cost)
        INSERT INTO records_cost3 VALUES (@product_id, @account_no)
    END;
END

--Enter a complaint associated with a customer and product
GO
CREATE PROCEDURE ip_query5
    @complaint_id INT,
    @description VARCHAR(64),
    @treatment_expected VARCHAR(64),
    @product_id INT,
    @date DATE
AS
BEGIN
    INSERT INTO complaint VALUES (@complaint_id, @description, @treatment_expected);
    INSERT INTO files VALUES (@complaint_id, @product_id, @date);
END;

--Enter an accident associated with an appropriate employee and product
GO
CREATE PROCEDURE ip_query6
    @accident_no INT,
    @days_lost INT,
    @product_id INT,
    @date DATE
AS
BEGIN
    INSERT INTO accident VALUES (@accident_no, @days_lost)
    INSERT INTO have VALUES (@product_id, @accident_no, @date)
END;

--Retrieve the date produced and time spent to produce a particular product
GO
CREATE PROCEDURE ip_query7
    @product_id INT
AS
BEGIN
    SELECT prod_date, prod_dur FROM makes
        WHERE makes.product_id = @product_id
END;

--Retrieve all products made by a particular worker
GO
CREATE PROCEDURE ip_query8
    @wname VARCHAR(64)
AS
BEGIN
    SELECT product_id FROM makes
    WHERE makes.wname = @wname
END;

--Retrieve the total number of errors a particular quality controller made.  This is the total number 
--of products certified by this controller and got some complaints
GO
CREATE PROCEDURE ip_query9
    @qname VARCHAR(64)
AS
BEGIN
    SELECT COUNT(tests.product_id) AS errors FROM tests, files
        WHERE tests.product_id = files.product_id
        AND qname = @qname;
END;

--Retrieve the total costs  of the  products  in the product3 category which were repaired at the 
--request of a particular quality controller
GO
CREATE PROCEDURE ip_query10
    @qname VARCHAR(64)
AS
BEGIN
    SELECT SUM(cost) as total_cost FROM records_cost3, product3_account, requests_repair
        WHERE product3_account.account_no = records_cost3.account_no
        AND records_cost3.product_id = requests_repair.product_id
        AND requests_repair.qname = @qname
    GROUP BY product3_account.account_no
END;

--Retrieve  all  customers  (in  name  order)  who  purchased  all  products  of  a  particular  color
GO
CREATE PROCEDURE ip_query11
    @color VARCHAR(64)
AS
BEGIN
    -- SELECT DISTINCT cname FROM purchases
    --     WHERE NOT EXISTS (
    --         (SELECT product_id FROM product2 WHERE color = @color)
    --             EXCEPT
    --         (SELECT product_id FROM product2  WHERE product2.product_id = purchases.product_id)
    --     )
    -- ORDER BY cname
    SELECT DISTINCT cname from purchases, product2
        WHERE product2.color = @color
        AND product2.product_id = purchases.product_id
    ORDER BY cname;
END;

--Retrieve all employees whose salary is above a particular salary
GO
CREATE PROCEDURE ip_query12
    @salary DECIMAL(18,2)
AS
BEGIN
    (SELECT qname AS name FROM quality_controller WHERE salary > @salary)
        UNION
    (SELECT wname AS name FROM worker WHERE salary > @salary)
        UNION
    (SELECT sname AS name FROM technical_staff WHERE salary > @salary)
END;

--Retrieve the total number of workdays lost due to accidents in repairing the products which got 
--complaints
GO
CREATE PROCEDURE ip_query13
AS
BEGIN
    SELECT SUM(days_lost) AS total_days_lost FROM accident, have, complaint_repair
        WHERE complaint_repair.product_id = have.product_id
        AND accident.accident_no = have.accident_no
    GROUP BY have.product_id
END;

--Retrieve the average cost of all products made in a particular year
GO
CREATE PROCEDURE ip_query14
    @year INT
AS
BEGIN
    SELECT AVG(cost) AS avg_cost FROM makes, 
        (SELECT * FROM product1_account UNION SELECT * FROM product2_account UNION SELECT * FROM product3_account) AS product_account, 
        (SELECT * FROM records_cost1 UNION SELECT * FROM records_cost2 UNION SELECT * FROM records_cost3) AS records_cost
    WHERE product_account.account_no = records_cost.account_no
    AND YEAR(prod_date) = @year
END;

-- Delete all accidents whose dates are in some range
GO
CREATE PROCEDURE ip_query15
    @date_begin DATE,
    @date_end DATE
AS
BEGIN TRANSACTION
    DELETE FROM have
        WHERE accident_no IN (SELECT accident_no FROM accident)
        AND have.date BETWEEN @date_begin AND @date_end;
    DELETE FROM accident -- remove dangling accident from have
        WHERE accident_no NOT IN (SELECT accident_no FROM have)
COMMIT;