using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Assignments
    {
        public Assignments()
        {
            Submission = new HashSet<Submission>();
        }

        public string Name { get; set; }
        public string Contents { get; set; }
        public DateTime? Due { get; set; }
        public uint AId { get; set; }
        public uint? Points { get; set; }
        public uint CategoryId { get; set; }

        public virtual AssignmentCategories Category { get; set; }
        public virtual ICollection<Submission> Submission { get; set; }
    }
}
