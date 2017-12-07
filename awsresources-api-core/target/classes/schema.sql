DROP TABLE IF EXISTS RESOURCE_CATEGORY;
DROP TABLE IF EXISTS RESOURCE;

DROP SEQUENCE IF EXISTS SEQ_RESOURCE;
create sequence SEQ_RESOURCE START WITH 1;

/*==============================================================*/
/* Table : RESOURCE_GATEGORY		                                        */
/*==============================================================*/
CREATE TABLE RESOURCE_CATEGORY (
	ID 				    BIGINT           not null,
	NAME 				VARCHAR(150)     not null,
	CONSTRAINT PK_RSC_CATEGORY PRIMARY KEY (ID)

);


/*==============================================================*/
/* Table : RESOURCES		                                        */
/*==============================================================*/
CREATE TABLE RESOURCE (
	ID 				    BIGINT           not null,
	CAT_ID         INT              not null,
	NAME 				VARCHAR(150)     not null,
	REGION              VARCHAR(150)     null,
	STATUS              VARCHAR(150)     null,
	RESOURCE_ID              VARCHAR(150)     null,
	TYPE              VARCHAR(150)     null,
	IMAGE_ID              VARCHAR(150)     null,
	VPC_ID              VARCHAR(150)     null,
	SUBNET_ID              VARCHAR(150)     null,
	ENDPOINT              VARCHAR(150)     null,
	RELATED_RESOURCES    INT default 0,
	CONSTRAINT PK_RESOURCE PRIMARY KEY (ID),
	CONSTRAINT U_RESOURCE UNIQUE (CAT_ID, REGION, RESOURCE_ID)
);

