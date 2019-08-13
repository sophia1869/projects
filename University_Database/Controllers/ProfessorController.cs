using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace LMS.Controllers
{
    [Authorize(Roles = "Professor")]
    public class ProfessorController : CommonController
    {
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Students(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Class(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Categories(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult CatAssignments(string subject, string num, string season, string year, string cat)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            return View();
        }

        public IActionResult Assignment(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Submissions(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Grade(string subject, string num, string season, string year, string cat, string aname, string uid)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            ViewData["uid"] = uid;
            return View();
        }

        /*******Begin code to modify********/


        /// <summary>
        /// Returns a JSON array of all the students in a class.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "dob" - date of birth
        /// "grade" - the student's grade in this class
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetStudentsInClass(string subject, int num, string season, int year)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join e in db.Enrolled on cl.ClassId equals e.ClassId
                join u in db.Users on e.UId equals u.UId
                select new
                {
                    fname = u.FName,
                    lname = u.LName,
                    uid = u.UId,
                    dob = u.Dob,
                    grade = e.Grade
                };

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            return Json(query.ToArray());
            
        }



        /// <summary>
        /// Returns a JSON array with all the assignments in an assignment category for a class.
        /// If the "category" parameter is null, return all assignments in the class.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The assignment category name.
        /// "due" - The due DateTime
        /// "submissions" - The number of submissions to the assignment
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class, 
        /// or null to return assignments from all categories</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInCategory(string subject, int num, string season, int year, string category)
        {
            if (category == null)
            {
                var query1 =
                    from cou in db.Courses
                    where cou.Department == subject && cou.Num == num.ToString()
                    join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                    where cl.Semester == season + year.ToString()
                    join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                    join a in db.Assignments on ac.CategoryId equals a.CategoryId
                    select new
                    {
                        aname = a.Name,
                        cname = ac.Name,
                        due = a.Due,
                        submissions =
                        from tempa in db.Assignments
                        where tempa.CategoryId == a.CategoryId
                        group tempa by tempa.CategoryId into ss
                        select ss.Count()
                    };
                
                /*
                foreach (var v in query1)
                {
                    System.Diagnostics.Debug.WriteLine(v);
                }
                System.Diagnostics.Debug.WriteLine(query1.ToList().Count);
                */

                return Json(query1.ToArray());
            }

            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                select new
                {
                    aname = a.Name,
                    cname = category,
                    due = a.Due,
                    submissions =
                    from tempa in db.Assignments
                    where tempa.CategoryId == a.CategoryId
                    group tempa by tempa.CategoryId into ss
                    select ss.Count()
                };
            /*
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            */
            return Json(query.ToArray());
        }


        /// <summary>
        /// Returns a JSON array of the assignment categories for a certain class.
        /// Each object in the array should have the folling fields:
        /// "name" - The category name
        /// "weight" - The category weight
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentCategories(string subject, int num, string season, int year)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                select new
                {
                    name = ac.Name,
                    weight = ac.Weight
                };

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            return Json(query.ToArray());
        }

        /// <summary>
        /// Creates a new assignment category for the specified class.
        /// If a category of the given class with the given name already exists, return success = false.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The new category name</param>
        /// <param name="catweight">The new category weight</param>
        /// <returns>A JSON object containing {success = true/false} </returns>
        public IActionResult CreateAssignmentCategory(string subject, int num, string season, int year, string category, int catweight)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                select ac;

            if (query.ToList().Count > 0)
            {
                return Json(new { success = false });
            }

            AssignmentCategories acn = new AssignmentCategories();
            /*
            var query2 =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                into couJoinCl
                from coucl in couJoinCl
                where coucl.Semester == season + ' ' + year.ToString()
                select coucl.ClassId;
            */
            
            var query2 =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                select cl.ClassId;

            System.Diagnostics.Debug.WriteLine(subject);
            System.Diagnostics.Debug.WriteLine(num);

            acn.Weight = Convert.ToUInt32(catweight);
            acn.Name = category;
            acn.ClassId = query2.First();
            db.AssignmentCategories.Add(acn);
            db.SaveChanges();
            return Json(new { success = true });
        }

        /// <summary>
        /// Creates a new assignment for the given class and category.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="asgpoints">The max point value for the new assignment</param>
        /// <param name="asgdue">The due DateTime for the new assignment</param>
        /// <param name="asgcontents">The contents of the new assignment</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult CreateAssignment(string subject, int num, string season, int year, string category, string asgname, int asgpoints, DateTime asgdue, string asgcontents)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                where a.Name == asgname
                select a;

            if (query.ToList().Count > 0)
            {
                return Json(new { success = false });
            }

            Assignments agn = new Assignments();
            var query2 =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                select ac.CategoryId;

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);

            agn.CategoryId = Convert.ToUInt32(query2.First());
            agn.Name = asgname;
            agn.Contents = asgcontents;
            agn.Due = asgdue;
            agn.Points = Convert.ToUInt32(asgpoints);
            db.Assignments.Add(agn);
            db.SaveChanges();
            return Json(new { success = true });
        }


        /// <summary>
        /// Gets a JSON array of all the submissions to a certain assignment.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "time" - DateTime of the submission
        /// "score" - The score given to the submission
        /// 
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetSubmissionsToAssignment(string subject, int num, string season, int year, string category, string asgname)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                where a.Name == asgname
                join s in db.Submission on a.AId equals s.AId
                join u in db.Users on s.UId equals u.UId
                select new
                {
                    fname = u.FName,
                    lname = u.LName,
                    uid = u.UId,
                    time = s.Time,
                    score = s.Score
                };

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            return Json(query.ToArray());
        }


        /// <summary>
        /// Set the score of an assignment submission
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <param name="uid">The uid of the student who's submission is being graded</param>
        /// <param name="score">The new score for the submission</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult GradeSubmission(string subject, int num, string season, int year, string category, string asgname, string uid, int score)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season +  year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                where a.Name == asgname
                join s in db.Submission on a.AId equals s.AId
                where s.UId == uid
                select s;
            
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);

            query.First().Score = Convert.ToUInt32(score);

            var query2 =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                select cl.ClassId;

            Enrolled en = new Enrolled();
            en.ClassId = query2.ToArray()[0];
            en.UId = uid;

            var query3 =
                from s in db.Submission
                where s.UId == uid
                join a in db.Assignments on s.AId equals a.AId
                select new
                {
                    points = a.Points,
                    score = s.Score
                };

            double scaledScore = 0;
            foreach(var v in query3){
                System.Diagnostics.Debug.WriteLine(v);
                scaledScore += Convert.ToDouble(v.score*1.0 / v.points);
            }
            scaledScore /= query3.Count();

            System.Diagnostics.Debug.WriteLine(scaledScore);
            if (scaledScore >= 0.9) { en.Grade = "A"; }
            else if (scaledScore >= 0.85) { en.Grade = "A-"; }
            else if (scaledScore >= 0.8) { en.Grade = "B+"; }
            else if (scaledScore >= 0.75) { en.Grade = "B"; }
            else if (scaledScore >= 0.7) { en.Grade = "B-"; }
            else if (scaledScore >= 0.6) { en.Grade = "C+"; }
            else if (scaledScore >= 0.5) { en.Grade = "C"; }
            else if (scaledScore >= 0.4) { en.Grade = "C-"; }
            else if (scaledScore >= 0.3) { en.Grade = "D+"; }
            else if (scaledScore >= 0.2) { en.Grade = "D"; }
            else if (scaledScore >= 0.1) { en.Grade = "D-"; }
            else { en.Grade = "E"; }

            db.Enrolled.Update(en);

            db.SaveChanges();
            return Json(new { success = true });
        }


        /// <summary>
        /// Returns a JSON array of the classes taught by the specified professor
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester in which the class is taught
        /// "year" - The year part of the semester in which the class is taught
        /// </summary>
        /// <param name="uid">The professor's uid</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var query =
                from cl in db.Classes
                where cl.Teacher == uid
                join cou in db.Courses on cl.CatalogId equals cou.CatalogId
                select new
                {
                    subject = cou.Department,
                    number = cou.Num,
                    name = cou.Name,
                    season = cl.Semester.Substring(0, cl.Semester.Length-4),
                    year = cl.Semester.Substring(cl.Semester.Length-4, cl.Semester.Length),
                };

            /*
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }

            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            */

            return Json(query.ToArray());

        }


        /*******End code to modify********/

    }
}