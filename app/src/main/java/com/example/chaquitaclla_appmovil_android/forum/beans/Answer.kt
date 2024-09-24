package com.example.chaquitaclla_appmovil_android.forum.beans

/**
 * {
 *   "answerId": 1,
 *   "authorId": 1,
 *   "questionId": 1,
 *   "answerText": "string"
 * }
 */
class Answer(
        var answerId: Int,
        var authorId: Int,
        var questionId: Int,
        var answerText: String
) {

}

class AnswerPost(
        var authorId: Int,
        var questionId: Int,
        var answerText: String
) {

}