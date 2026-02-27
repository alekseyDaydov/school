1. Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах
(достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
select s.name, s.age, f.name as faculty
     from student as s
        join faculty as f
      on s.faculty_id = f.id;

2.Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.
select s.name, s.age
     from student as s
        join avatar as a
      on s.id = a.student_id
     where a.media_type is not null;