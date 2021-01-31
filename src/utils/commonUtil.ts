
export const isEmpty = (obj: Object) => {
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            return false;
        }
    }
    return true;
};

function padNumberFunc(d: number) {
    return (d < 10) ? '0' + d.toString() : d.toString();
}

export const padNumber = padNumberFunc;

const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
];

export const getDate = (date: Date) => {
    return padNumberFunc(date.getDate()) + '.' + (monthNames[date.getMonth()]) + '.' + date.getFullYear();
};

export const getTime = (date: Date) => {
    return padNumberFunc(date.getHours()) + ':' + padNumberFunc(date.getMinutes());
};

export const getDateTime = (date: Date) => {
    return `${getDate(new Date(date))}  ${getTime(new Date(date))}`
};

export const msToTime = (duration: any) => {
    let d = new Date(+duration);
    let datetext = d.toTimeString().split(' ')[0];
    let hours = (datetext.split(':')[0]);
    let minutes = (datetext.split(':')[1]);
    return (hours + ':' + minutes);
};

export const getModifierName = (modifiers: any, index: number) => {
    return (index === 0 ? modifiers.modifier : ',  ' + modifiers.modifier);
};

export const renderProps = (prop: any) => {
    return (prop.unit ? prop.unit.symbol : '');
};

export const stringToByteArray = (str: string) => {
    let binaryArray = new Uint8Array(str.length);
    Array.prototype.forEach.call(binaryArray, function (el, idx, arr) { arr[idx] = str.charCodeAt(idx); } );
    return binaryArray;
}

export const byteArrayToString = (aBytes: []) =>{
let sView = '';
  for (let nPart, nLen = aBytes.length, nIdx = 0; nIdx < nLen; nIdx++) {
      nPart = aBytes[nIdx];
      sView += String.fromCharCode(
          nPart > 251 && nPart < 254 && nIdx + 5 < nLen ? /* six bytes */
              /* (nPart - 252 << 30) may be not so safe in ECMAScript! So...: */
              (nPart - 252) * 1073741824 + (aBytes[++nIdx] - 128 << 24) +
              (aBytes[++nIdx] - 128 << 18) + (aBytes[++nIdx] - 128 << 12) +
              (aBytes[++nIdx] - 128 << 6) + aBytes[++nIdx] - 128
          : nPart > 247 && nPart < 252 && nIdx + 4 < nLen ? /* five bytes */
              (nPart - 248 << 24) + (aBytes[++nIdx] - 128 << 18) + 
              (aBytes[++nIdx] - 128 << 12) + (aBytes[++nIdx] - 128 << 6) + aBytes[++nIdx] - 128
          : nPart > 239 && nPart < 248 && nIdx + 3 < nLen ? /* four bytes */
              (nPart - 240 << 18) + (aBytes[++nIdx] - 128 << 12) + 
              (aBytes[++nIdx] - 128 << 6) + aBytes[++nIdx] - 128
          : nPart > 223 && nPart < 240 && nIdx + 2 < nLen ? /* three bytes */
              (nPart - 224 << 12) + (aBytes[++nIdx] - 128 << 6) + aBytes[++nIdx] - 128
          : nPart > 191 && nPart < 224 && nIdx + 1 < nLen ? /* two bytes */
              (nPart - 192 << 6) + aBytes[++nIdx] - 128
          : /* nPart < 127 ? */ /* one byte */
              nPart
      );
  }
  return sView;

}
