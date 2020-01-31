import React from 'react'

const CheckBox = (props) => {

    return( 
    <div className={props.divClassName+ " input-field"}>
    <h6>{props.title}</h6>
      {props.options.map(option => {
        return (
          <span 
          key={option}
          className={"checkbox"}>
            <input
              className={props.name}
              id = {option}
              name={props.name}
              onChange={props.handleChange}
              value={option}
              checked={props.selectedOptions.indexOf(option) > -1}
              type="checkbox" />
            <label htmlFor={option}>{option}</label>
          </span>
        );
      })}
    </div>
);

}
export default CheckBox;