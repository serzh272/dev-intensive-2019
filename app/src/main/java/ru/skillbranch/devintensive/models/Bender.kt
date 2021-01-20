package ru.skillbranch.devintensive.models

import java.lang.StringBuilder

class Bender(var status:Status = Status.NORMAL, var question:Question = Question.NAME) {
    fun askQuestion():String = when (question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    private fun isAnswerValid(question:Question, answer:String):Pair<Boolean, String>{
        return when (question){
            Question.NAME -> "^[A-ZА-Я].*".toRegex().containsMatchIn(answer) to "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> "^[a-zа-я].*".toRegex().containsMatchIn(answer) to "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> !"\\d+".toRegex().containsMatchIn(answer) to "Материал не должен содержать цифр"
            Question.BDAY -> "^\\d+$".toRegex().containsMatchIn(answer) to "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> "^\\d{7}$".toRegex().containsMatchIn(answer) to "Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> true to ""
        }
    }
    fun listenAnswer(answer:String):Pair<String, Triple<Int, Int, Int>>{
        val (isValid, error) = isAnswerValid(question, answer)
        if(isValid){
            if(question.answers.contains(answer) || question.answers.contains(answer.toLowerCase())){
                question = question.nextQuestion()
                //status = Status.NORMAL
                return "Отлично - ты справился!\n${question.question}" to status.color
            }else{
                if(status == Status.CRITICAL){
                    status = Status.NORMAL
                    question = Question.NAME
                    return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                }
                status = status.nextStatus()
                return "Это неправильный ответ!\n${question.question}" to status.color
            }
        }
        else{
            return "$error\n${question.question}" to status.color
        }
    }

    enum class Status(val color:Triple<Int, Int, Int>){
        NORMAL (Triple(255, 255, 255)),
        WARNING (Triple(255, 120, 0)),
        DANGER (Triple(255, 60, 60)),
        CRITICAL (Triple(255, 0, 0));
        fun nextStatus(): Status{
            return if(this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            }else{
                values()[0]
            }
        }
    }
    enum class Question(val question: String, val answers:List<String>){
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion():Question
    }
}