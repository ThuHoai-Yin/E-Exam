create table userTbl(
	userID int identity(1,1) primary key,
	username varchar(20) not null,
	userRole varchar(10) not null,
	hashedPassword varbinary(64) not null,
	salt varbinary(32) not null,
)

create table userDetailTbl(
	userID int primary key references userTbl(userID),
	fullname nvarchar(30) not null,
	email varchar(40) not null,
)

create table bankTbl(
	bankID int identity(1,1) primary key,
	courseCode varchar(10) not null,
	creatorID int foreign key references userTbl(userID) not null,
	dateCreated datetime not null,
)

create table questionTbl(
	questionID int identity(1,1) primary key,
	bankID int foreign key references bankTbl(bankID) not null,
	mark tinyint not null,
	questionContent nvarchar(MAX) not null,
)

create table answerTbl(
	answerID int identity(1,1) primary key,
	questionID int foreign key references questionTbl(questionID) not null,
	answerContent nvarchar(100) not null,
	isCorrect bit not null,
)

create table examTbl(
	examCode varchar(25) primary key,
	bankID int foreign key references bankTbl(bankID) not null,
	startDate datetime,
	endDate datetime,
	duration smallint check (duration > 0),
	constraint timeCS check (startDate is null or endDate is null or startDate < endDate)	
)

create table examDetailTbl(
	examCode varchar(25) foreign key references examTbl(examCode) not null,
	questionID int foreign key references questionTbl(questionID) not null,
	constraint tAll primary key (examCode, questionID)
)

create table recordTbl(
	examCode varchar(25) foreign key references examTbl(examCode) not null,
	studentID int foreign key references userTbl(userID) not null,
	recordID int identity(1, 1) unique not null,
	examDate datetime not null,
	dateSummited datetime,
	constraint rPK primary key(examCode, studentID)
)

create table recordDetailTbl(
	recordID int foreign key references recordTbl(recordID) not null,
	answerID int foreign key references answerTbl(answerID) not null,
	constraint rDPK primary key(recordID, answerID)
)