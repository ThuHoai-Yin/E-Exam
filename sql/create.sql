create table roleTbl(
	roleID tinyint identity(1,1) primary key,
	roleName varchar(20) not null,
	canTakeExam bit not null,
    canManageAccount bit not null,
    canManageBank bit not null,
    canManageExam bit not null,
)

create table userTbl(
	userID int identity(1,1) primary key,	
	username varchar(20) not null,	
	fullname nvarchar(30) not null,
	email varchar(40) not null,
	roleID tinyint foreign key references roleTbl(roleID) not null,
	hashedPassword varbinary(64) not null,
	salt varbinary(32) not null,
)

create table bankTbl(
	bankID int identity(1,1) primary key,
	courseName nvarchar(50) not null,
	bankName nvarchar(50) not null,
	creatorID int not null,
	dateCreated datetime not null,
	constraint bT_cI_FK foreign key (creatorID) references userTbl(userID) on delete cascade, 
)

create table questionTbl(
	questionID int identity(1,1) primary key,
	bankID int,
	questionContent nvarchar(MAX) not null,
	constraint qT_bI_FK foreign key (bankID) references bankTbl(bankID) on delete set null,
)

create table answerTbl(
	answerID int identity(1,1) primary key,
	questionID int not null,
	answerContent nvarchar(100) not null,
	isCorrect bit not null,
	constraint qT_aI_FK foreign key (questionID) references questionTbl(questionID) on delete cascade, 
) 

create table examTbl(
	examCode varchar(25) primary key,
	examName nvarchar(50) not null,
	openDate datetime,
	closeDate datetime,
	creatorID int not null,
	duration smallint check (duration > 0),
	constraint eT_cI_FK foreign key (creatorID) references userTbl(userID) on delete cascade, 
	constraint eT_CK check (openDate is null or closeDate is null or openDate < closeDate)	
)

create table examDetailTbl(
	examCode varchar(25) not null,
	questionID int not null, 
	constraint eD_eC_FK foreign key (examCode) references examTbl(examCode) on delete cascade, 
	constraint eD_qI_FK foreign key (questionID) references questionTbl(questionID) on delete cascade, 
	constraint eD_PK primary key (examCode, questionID)
)

create table recordTbl(
	examCode varchar(25) not null,
	takerID int not null,
	recordID int identity(1, 1) unique not null,
	examDate datetime not null,
	dateSubmitted datetime,
	constraint rT_eC_FK foreign key (examCode) references examTbl(examCode) on delete cascade,
	constraint rT_tI_FK foreign key (takerID) references userTbl(userID),
	constraint rT_PK primary key(examCode, takerID)	
)

create table recordDetailTbl(
	recordID int not null,
	answerID int foreign key references answerTbl(answerID) not null,	
	constraint rIFK foreign key (recordID) references recordTbl(recordID) on delete cascade,
	constraint rDPK primary key(recordID, answerID)
)

insert into examTbl(examCode, examName, openDate, closeDate, creatorID, duration) values (?,?,?,?,?,?)
insert into examDetailTbl 
select top ? ?, qt.questionID 
from questionTbl qt 
where qt.bankID = ?
order by rand()