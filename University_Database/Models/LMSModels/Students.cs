using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Students
    {
        public Students()
        {
            Enrolled = new HashSet<Enrolled>();
            Submission = new HashSet<Submission>();
        }

        public string UId { get; set; }
        public string Department { get; set; }

        public virtual Departments DepartmentNavigation { get; set; }
        public virtual Users U { get; set; }
        public virtual ICollection<Enrolled> Enrolled { get; set; }
        public virtual ICollection<Submission> Submission { get; set; }
    }
}
