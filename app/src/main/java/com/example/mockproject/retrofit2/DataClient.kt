package com.example.mockproject.retrofit2

import android.support.v4.view.ViewPager
import android.text.Editable
import com.example.mockproject.GroupFragment.Pending.ListPutPending
import com.example.mockproject.GroupFragment.Pending.PendingResponse
import com.example.mockproject.GroupFragment.Pending.Putpending
import com.example.mockproject.GroupFragment.model.DataResponseCreateQueue
import com.example.mockproject.GroupFragment.model.DataResponseGetGroup
import com.example.mockproject.GroupFragment.model.ListMemberWantToJoinGroup

import com.example.mockproject.GroupFragment.model.MemberSearchResponse

import com.example.mockproject.GroupFragment.model.createMember.DataPostServer
import com.example.mockproject.GroupFragment.model.createMember.DataResponseCreateMember


import com.example.mockproject.ListAttendance.Model.ListGetAttendance
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance
import com.example.mockproject.Sprint5.AttendanceEventNT.Model.ListGetAttendanceEvent

import com.example.mockproject.Sprint5.createEventScreen.model.CreateEventModelPostServer
import com.example.mockproject.Sprint5.calendarScreen.model.EventResponses
import com.example.mockproject.Sprint5.calendarScreen.model.GetEventMonthYearResponses
import com.example.mockproject.Sprint5.calendarScreen.model.HistoryEventResponses
import com.example.mockproject.getmember.NewCaptain
import com.example.mockproject.homeScreen.addMember.DataPostServerAddMember
import com.example.mockproject.homeScreen.listGroup.createQueue.DataPostServerCreateQueue
import com.example.mockproject.homeScreen.listGroup.model.DataServerResponeGetGroups


import com.example.mockproject.getmember.ResponeMembers
import com.example.mockproject.model.ListCreateContent
import com.example.mockproject.model.Posts
import com.example.mockproject.model.UserContent
import com.example.mockproject.model.modelCreateCourse.CreateCourse
import com.example.mockproject.model.modelPostAttendance.ListPostMemberUserId
import com.example.mockproject.model.modelPostAttendanceEvent.ListPostMemberUserIdEvent
import com.example.mockproject.model.modelUser.ResponseUser
import com.example.mockproject.model.modelUser.UserEntity
import com.example.mockproject.util.BaseDataResponseServer
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface DataClient {
    @POST("v1/user")
    fun createUser(@Body user: UserEntity): Call<ResponseUser>

//    @GET("v1/contents/getAll")
//    fun getContentsToList(): Call<BaseDataResponseServer<>>

    // Get a List Course
    @GET("v1/courses")
    fun getGroup(
            @Query("userId") userId: String?
    ): Call<DataServerResponeGetGroups>

    @POST("v1/courses")
    fun createGroup(@Body post:CreateCourse):Call<BaseDataResponseServer<DataResponseGetGroup>>
    @PUT("v1/courses/{courseId}")
    fun putCourse(@Body put:CreateCourse,@Path("courseId") courseId: String,@Query("userId") userId: String,@Query("code") code: Int ):Call<getTakeAttendance>

    // Get a Course
    @GET("v1/courses/{courseId}")
    fun getGroupById(
            @Path("courseId") courseId: String,
            @Query("userId") userId: String,
            @Query("page") page: Int,
            @Query("size") size: Int
    ): Call<BaseDataResponseServer<DataResponseGetGroup>>


    // update a content
    @PUT("v1/contents/{contentId}")
    fun updateContent(@Path("contentId") contentId: String,
                      @Body userContent: UserContent): Call<BaseDataResponseServer<String>>


    @POST("v1/members/userId")
    fun addMemberJoinToGroup(
            @Body dataPostServerAddMember: DataPostServerAddMember,
            @Query("userId") userId: String?
    ): Call<DataPostServerAddMember>

    @POST("v1/queues")
    fun createQueue(@Body dataPostServerCreateQueue: DataPostServerCreateQueue):
            Call<DataResponseCreateQueue>


    @GET("v1/members/{courseId}")
    fun getMembersAPI(
            @Path("courseId")
            courseId: String
    ): Call<ResponeMembers>

    @GET("v1/queues")
    fun getListRequest(@Query("courseId") courseId: String):
            Call<BaseDataResponseServer<ListMemberWantToJoinGroup>>

    @DELETE("v1/queues/{queueId}")
    fun deleteQueue(
            @Path("queueId") queueId: String?,
            @Query("userId") userId: String?,
            @Query("code") code: Int
    ): Call<BaseDataResponseServer<Any>>

    @DELETE("v1/contents/{contentId}")
    fun deleteContent(
            @Path("contentId") contentId: String,
            @Query("userId") userId: String
    ): Call<BaseDataResponseServer<Any>>

    @DELETE("v1/events/{eventId}")
    fun deleteEvent(
            @Path("eventId") contentId: String,
            @Query("userId") userId: String
    ): Call<BaseDataResponseServer<Any>>

    @GET("v1/attendance")
    fun getAttendances(@Query("contentId") contentId: String?, @Query("courseId") courseId: String?)
            : Call<ListGetAttendance>

    @POST("v1/attendance")
    fun postAttendances(@Body post: ListPostMemberUserId)
            : Call<getTakeAttendance>


    @GET("v1/attendanceEvent")
    fun getAttendancesEvent(@Query("eventId") eventId: String?, @Query("courseId") courseId: String?)
            : Call<ListGetAttendanceEvent>

    @POST("v1/attendanceEvent")
    fun postAttendancesEvent(@Body post: ListPostMemberUserIdEvent)
            : Call<getTakeAttendance>

    @POST("v1/contents?")
    fun createContent(@Body post: Posts,@Query("userId") userId: String): Call<ListCreateContent>


    @POST("v1/members")
    fun createMembers(
            @Query("userId") userId: String,
            @Body post: DataPostServer)
            : Call<BaseDataResponseServer<DataResponseCreateMember>>

    @GET("v1/user")
    fun getSearchMember(@Query("courseId") courseId: String,
                        @Query("textSearch") textSearch: Editable)
            : Call<BaseDataResponseServer<MemberSearchResponse>>

    // delete member
    @DELETE("v1/members/{memberUserId}")
    fun deleteMember(@Path("memberUserId") memberUserId: String,
                     @Query("userId") userId: String,
                     @Query("courseId") courseId: String
    ): Call<BaseDataResponseServer<String>>

    @PUT("v1/courses/{courseId}")
    fun setCaptain(
            @Path("courseId") courseId: String,
            @Query("userId") captainId: String,@Query("code") code: Int,
            @Body member: NewCaptain
    ): Call<BaseDataResponseServer<String>>

    @GET("v1/events/getEventsByMonth?")// chưa test kiểm tra lại đường link và tên biến
    fun getEventByMonth(@Header("startMonthYear") startMonth: String,
                        @Query("courseId") courseId: String
    ): Call<BaseDataResponseServer<GetEventMonthYearResponses>>

    @GET("v1/events/getEventsByDay?")//chưa test kiểm tra lại đường link và tên biến
    fun getEventByDay(@Header("startDateTime") startDateTime : String?,
                      @Query("courseId") courseId: String
    ): Call<BaseDataResponseServer<EventResponses>>

    @POST("v1/events/")
    fun createEvent(@Body createEventModelPostServer: CreateEventModelPostServer,
                    @Query("userId") userId: String,
                    @Query("contentId") contentId: String?
    ): Call<BaseDataResponseServer<Any>>

    @POST("v1/events/")
    fun createEventNotEvent(@Body createEventModelPostServer: CreateEventModelPostServer,
                    @Query("userId") userId: String
    ): Call<BaseDataResponseServer<Any>>

    @PUT("v1/events/{eventId}")
    fun editEvent(
            @Path("eventId") eventId: String,
            @Query("userId") userId: String,
            @Body valueEvent: CreateEventModelPostServer

    ): Call<BaseDataResponseServer<Any>>

    @GET("v1/events/getHistoryEvent")
    fun getHistoryEvent(@Query("courseId") courseId: String): Call<BaseDataResponseServer<HistoryEventResponses>>

    @GET("v1/user/{userId}")
    fun getProfileUser(
            @Path("userId") userId: String
    ): Call<ResponseUser?>

    @GET("v1/pendings?")
    fun getPending(@Query("courseId")courseId: String,@Query("userId") userId: String):Call<BaseDataResponseServer<PendingResponse>>
    @DELETE("v1/pendings/{pendingId}?")
    fun deletePending(@Path("pendingId") pendingId:String,@Query("code") code: Int):Call<getTakeAttendance>
    @PUT("v1/pendings/{pendingId}")
    fun putPending(@Body postpending:Putpending,@Path("pendingId")pendingId:String,@Query("code") code: Int):Call<ListPutPending>

    @DELETE("v1/user/{userId}")
    fun logOutServer(
            @Path("userId") userId: String
    ): Call<BaseDataResponseServer<String>>

    @PUT("v1/user/{userId}")
    fun changeProfileUser(
            @Path("userId") userId: String,
            @Body user: UserEntity
    ): Call<BaseDataResponseServer<String>>

    @Multipart
    @POST("v1/user/upload")
    fun uploadImageApi(
            @Query("userId") userId: String,
            @Part file: MultipartBody.Part
    ): Call<BaseDataResponseServer<String>>

    @Multipart
    @POST("v1/courses/upload")
    fun uploadImageCourse(@Query("courseId")courseId: String,
                          @Query("userId") userId: String,
                          @Query("code") code: Int,
                          @Part file:MultipartBody.Part): Call<BaseDataResponseServer<String>>

}