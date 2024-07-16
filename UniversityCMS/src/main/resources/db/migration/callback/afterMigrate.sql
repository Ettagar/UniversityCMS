TRUNCATE TABLE university.classrooms CASCADE;
TRUNCATE TABLE university.course_student CASCADE;
TRUNCATE TABLE university.course_teacher CASCADE;
TRUNCATE TABLE university.courses CASCADE;
TRUNCATE TABLE university.groups CASCADE;
TRUNCATE TABLE university.lesson_types CASCADE;
TRUNCATE TABLE university.persons CASCADE;
TRUNCATE TABLE university.roles CASCADE;
TRUNCATE TABLE university.schedule_student CASCADE;
TRUNCATE TABLE university.schedules CASCADE;
TRUNCATE TABLE university.user_roles CASCADE;
TRUNCATE TABLE university.users CASCADE;

ALTER SEQUENCE university.classrooms_classroom_id_seq RESTART WITH 1;
ALTER SEQUENCE university.courses_course_id_seq RESTART WITH 1;
ALTER SEQUENCE university.groups_group_id_seq RESTART WITH 1;
ALTER SEQUENCE university.lesson_types_lesson_type_id_seq RESTART WITH 1;
ALTER SEQUENCE university.persons_person_id_seq RESTART WITH 1;
ALTER SEQUENCE university.roles_role_id_seq RESTART WITH 1;
ALTER SEQUENCE university.schedules_schedule_id_seq RESTART WITH 1;
ALTER SEQUENCE university.users_user_id_seq RESTART WITH 1;