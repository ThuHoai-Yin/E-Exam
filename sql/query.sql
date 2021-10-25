declare @examCode varchar(25) = 'abc123'
declare @studentID int = 2

select
	qt.questionID as questionID,
	qt.mark as mark,
	at2.answerID as answerID,
	at2.isCorrect as isCorrect
into #temp1
from	
	examTbl et 
inner join examDetailTbl edt on
	et.examCode = edt.examCode
inner join questionTbl qt on
	qt.questionID = edt.questionID
inner join answerTbl at2 on
	qt.questionID = at2.questionID
where et.examCode = @examCode

select rdt.answerID 
into #temp2
from recordTbl rt
inner join recordDetailTbl rdt on rt.recordID = rdt.recordID 
inner join #temp1 ans on rdt.answerID = ans.answerID and ans.isCorrect = 0

-- Wrong selected answers
select * from #temp2

-- Correct answers
select rdt.answerID from recordTbl rt 
inner join recordDetailTbl rdt on rt.recordID = rdt.recordID 
inner join #temp1 ans on rdt.answerID = ans.answerID and ans.isCorrect = 1

-- Correct not selected answers
select ans.answerID from #temp1 ans
where ans.isCorrect = 1 and ans.answerID not in (
select
	rdt.answerID
from
	recordTbl rt
inner join recordDetailTbl rdt on
	rt.recordID = rdt.recordID )

	
drop table #temp1
drop table #temp2

delete from questionTbl
where bankID is null and questionID not in (
	select questionID from examDetailTbl
)