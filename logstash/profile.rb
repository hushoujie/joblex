def filter(event)
  educations = [{
                    'school' => event.get('school'),
                    'field' => event.get('field'),
                    'degree' => event.get('degree'),
                    'location' => event.get('ed_location'),
                    'start_date' => event.get('ed_start_date'),
                    'end_date' => event.get('ed_end_date'),
                    'summary' => event.get('ed_summary')
                }]
  event.set('educations', educations)
  event.remove('school')
  event.remove('field')
  event.remove('degree')
  event.remove('ed_location')
  event.remove('ed_start_date')
  event.remove('ed_end_date')
  event.remove('ed_summary')
  experiences = [{
                     'company' => event.get('company'),
                     'position' => event.get('position'),
                     'location' => event.get('ex_location'),
                     'start_date' => event.get('ex_start_date'),
                     'end_date' => event.get('ex_end_date'),
                     'summary' => event.get('ex_summary')
                 }]
  event.set('experiences', experiences)
  event.remove('company')
  event.remove('position')
  event.remove('ex_location')
  event.remove('ex_start_date')
  event.remove('ex_end_date')
  event.remove('ex_summary')
  return [event]
end
