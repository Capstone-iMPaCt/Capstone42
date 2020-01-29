import React from 'react'

const Input = (props) => {
    return (  
  <div 
    id={(props.divId ? props.divId:'')} 
    hidden={props.divHidden} 
    className={(props.divClassName ? props.divClassName: '') + " input-field"}>
  <label htmlFor={props.name} className={"input " + (props.activeLabel ? props.activeLabel: '')}>
    {(props.icon ? <i className="material-icons left">{props.icon}</i>: '')}
    {props.title}
  </label>
    <input
      id={props.name}
      name={props.name}
      type={props.type}
      value={props.value}
      onChange={props.handleChange}
      placeholder={props.placeholder} 
      required={props.required}
      pattern={props.pattern}
      className={props.className}
    />
  </div>
)
}

export default Input;