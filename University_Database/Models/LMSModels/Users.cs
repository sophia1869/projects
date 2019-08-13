using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Users
    {
        public DateTime? Dob { get; set; }
        public string UId { get; set; }
        public string FName { get; set; }
        public string LName { get; set; }

        public virtual Administrators Administrators { get; set; }
        public virtual Professors Professors { get; set; }
        public virtual Students Students { get; set; }
    }
}
