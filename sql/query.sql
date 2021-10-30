select rt.recordID, rt.takerID, ut.fullname, rt.examDate, rt.dateSubmitted, count(rdt.answerID) as numOfCorrectAnswers, count(at2.answerID) as numOfAnswers from recordTbl rt
inner join userTbl ut on rt.takerID = ut.userID
inner join examDetailTbl edt on rt.examCode = edt.examCode
inner join answerTbl at2 on edt.questionID = at2.questionID
left outer join recordDetailTbl rdt on at2.answerID = rdt.answerID
where at2.isCorrect = 1
group by rt.recordID, rt.takerID, ut.fullname, rt.examDate, rt.dateSubmitted