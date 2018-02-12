-- (C) 2016 Masato Kokubo

-- for PostgreSQL

-- Numbering
DROP TABLE IF EXISTS Numbering;
CREATE TABLE Numbering (
    tableName   VARCHAR(32) NOT NULL,
    nextId      INT         NOT NULL,

    PRIMARY KEY(tableName)
);

-- Contact
DROP TABLE IF EXISTS Contact;
CREATE TABLE Contact (
    id          INT         NOT NULL,
    firstName   VARCHAR(20) NOT NULL,
    lastName    VARCHAR(20) NOT NULL,
    birthday    DATE            NULL,
    addressId   INT             NULL,

    updateCount INT         NOT NULL,
    created     TIMESTAMP   NOT NULL,
    updated     TIMESTAMP   NOT NULL,

    PRIMARY KEY(id)
);

-- Address
DROP TABLE IF EXISTS Address;
CREATE TABLE Address (
    id          INT         NOT NULL,
    postCode    VARCHAR(10)     NULL,
    address1    VARCHAR(20)     NULL,
    address2    VARCHAR(20)     NULL,
    address3    VARCHAR(20)     NULL,
    address4    VARCHAR(20)     NULL,

    updateCount INT         NOT NULL,
    created     TIMESTAMP   NOT NULL,
    updated     TIMESTAMP   NOT NULL,

    PRIMARY KEY(id)
);

-- Phone
DROP TABLE IF EXISTS Phone;
CREATE TABLE Phone (
    id          INT         NOT NULL,
    contactId   INT         NOT NULL,
    phoneNumber VARCHAR(12) NOT NULL,

    updateCount INT         NOT NULL,
    created     TIMESTAMP   NOT NULL,
    updated     TIMESTAMP   NOT NULL,

    PRIMARY KEY(id)
);

-- Product
DROP TABLE IF EXISTS Product;
CREATE TABLE Product (
    id          INT         NOT NULL,
    productName VARCHAR(20) NOT NULL,
    price       INT         NOT NULL,
    productSize CHAR   ( 2)     NULL,
    color       VARCHAR(20)     NULL,

    updateCount INT         NOT NULL,
    created     TIMESTAMP   NOT NULL,
    updated     TIMESTAMP   NOT NULL,

    PRIMARY KEY(id)
);

-- Sale
DROP TABLE IF EXISTS Sale;
CREATE TABLE Sale (
    id          INT         NOT NULL,
    contactId   INT         NOT NULL,
    saleDate    DATE        NOT NULL,
    taxRate     SMALLINT    NOT NULL,

    updateCount INT         NOT NULL,
    created     TIMESTAMP   NOT NULL,
    updated     TIMESTAMP   NOT NULL,

    PRIMARY KEY(id)
);

-- SaleItem
DROP TABLE IF EXISTS SaleItem;
CREATE TABLE SaleItem (
    saleId      INT         NOT NULL,
    itemIndex   SMALLINT    NOT NULL,
    productId   INT         NOT NULL,
    quantity    SMALLINT    NOT NULL,

    PRIMARY KEY(saleId, itemIndex)
);



-- Various
DROP TABLE IF EXISTS Various;
CREATE TABLE Various (
    id               INT              NOT NULL,

    booleanPValue    BOOLEAN          DEFAULT FALSE NOT NULL,
    char1PValue      CHAR(1)          DEFAULT ' '   NOT NULL,
    tinyIntPValue    SMALLINT         DEFAULT 0     NOT NULL, -- instead of TINYINT type
    smallIntPValue   SMALLINT         DEFAULT 0     NOT NULL,
    intPValue        INT              DEFAULT 0     NOT NULL,
    bigIntPValue     BIGINT           DEFAULT 0     NOT NULL,
    floatPValue      FLOAT            DEFAULT 0     NOT NULL,
    doublePValue     DOUBLE PRECISION DEFAULT 0     NOT NULL,

    booleanValue     BOOLEAN                 ,
    char1Value       CHAR(1)                 ,
    tinyIntValue     SMALLINT                , -- instead of TINYINT type
    smallIntValue    SMALLINT                ,
    intValue         INT                     ,
    bigIntValue      BIGINT                  ,
    floatValue       FLOAT                   ,
    doubleValue      DOUBLE PRECISION        ,

    decimalValue     DECIMAL(12,2)           ,

    dateValue        DATE                    ,
    timeValue        TIME                    ,
    timeTZValue      TIME WITH TIME ZONE     ,
    dateTimeValue    TIMESTAMP               , -- instead of DATETIME type
    timestampValue   TIMESTAMP               ,
    timestampTZValue TIMESTAMP WITH TIME ZONE,

    longDate         BIGINT                  , -- since 1.8.0
    longTime         BIGINT                  , -- since 1.8.0
    longTimestamp    BIGINT                  , -- since 1.8.0

    charValue        CHAR   (20)             ,
    varCharValue     VARCHAR(40)             ,

    binaryValue      BYTEA                   , -- instead of BINARY   (20) type
    varBinaryValue   BYTEA                   , -- instead of VARBINARY(40) type

    textValue        TEXT                    ,
    blobValue        BYTEA                   ,
    jsonValue        JSON                    ,
    jsonbValue       JSONB                   ,

    booleans         BOOLEAN          ARRAY  ,
    shorts           SMALLINT         ARRAY  ,
    ints             INT              ARRAY  ,
    longs            BIGINT           ARRAY  ,
    floats           FLOAT            ARRAY  ,
    doubles          DOUBLE PRECISION ARRAY  ,
    decimals         DECIMAL(12,2)    ARRAY  ,
    texts            TEXT             ARRAY  ,
    dates            DATE             ARRAY  ,
    times            TIME             ARRAY  ,
    timestamps       TIMESTAMP        ARRAY  ,

    PRIMARY KEY(id)
);
